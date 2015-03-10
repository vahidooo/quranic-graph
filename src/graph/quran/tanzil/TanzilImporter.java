package graph.quran.tanzil;

import base.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by vahidoo on 3/5/15.
 */
public class TanzilImporter implements Importer {

    private static final String SURAS_TAG = "suras";
    private static final String SURA_TAG = "sura";
    private static final String AYA_TAG = "aya";


    private static final String[] SURA_NUMERIC_ATTRS = {NodeProperties.General.index, NodeProperties.Chapter.order, NodeProperties.Chapter.ayas};
    private static final String[] SURA_STRING_ATTRS = {NodeProperties.Chapter.type, NodeProperties.Chapter.name};


    private File metadataFile;
    private File uthmaniFile;

    public TanzilImporter(String uthmaniFileAddr, String metadataFileAddr) {
        uthmaniFile = new File(uthmaniFileAddr);
        metadataFile = new File(metadataFileAddr);
    }

    @Override
    public void doImport(GraphDatabaseService graphDB) throws ParserConfigurationException, IOException, SAXException {

        importChapters(graphDB);
        importVerses(graphDB);


    }

    private void importVerses(GraphDatabaseService graphDB) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(uthmaniFile);

        NodeList suras = ((Element) doc.getDocumentElement()).getElementsByTagName(SURA_TAG);

        try (Transaction tx = graphDB.beginTx()) {

            for (int i = 0; i < suras.getLength(); i++) {
                System.out.println("update chapter #" + (i+1));
                Node sura = suras.item(i);

                int chapterIndex = Integer.parseInt(sura.getAttributes().getNamedItem(NodeProperties.General.index).getNodeValue());
                org.neo4j.graphdb.Node chapterNode = graphDB.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.General.index, chapterIndex).getSingle();

                NodeList ayas = ((Element) sura).getElementsByTagName(AYA_TAG);
                for (int j = 0; j < ayas.getLength(); j++) {
                    Node aya = ayas.item(j);

                    int verseIndex = Integer.parseInt(aya.getAttributes().getNamedItem(NodeProperties.General.index).getNodeValue());
                    String text = aya.getAttributes().getNamedItem(NodeProperties.Verse.text).getNodeValue();
                    org.neo4j.graphdb.Node verseNode = graphDB.createNode(NodeLabels.VERSE);
                    verseNode.setProperty(NodeProperties.General.index, verseIndex);
                    verseNode.setProperty(NodeProperties.Verse.text, text);

                    String address = NodeUtils.getVerseAddress(chapterIndex, verseIndex);
                    NodeUtils.setPropertyAndIndex(verseNode, NodeProperties.General.address, GraphIndices.VerseIndex, address);

                    chapterNode.createRelationshipTo(verseNode, RelationshipTypes.CONTAINS_VERSE);
                }


            }
            tx.success();
        }

    }

    private void importChapters(GraphDatabaseService graphDB) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(metadataFile);

        NodeList suras = ((Element) doc.getDocumentElement().getElementsByTagName(SURAS_TAG).item(0)).getElementsByTagName(SURA_TAG);

        try (Transaction tx = graphDB.beginTx()) {

            for (int i = 0; i < suras.getLength(); i++) {
                System.out.println("add chapter #" + (i+1));
                Node sura = suras.item(i);

                org.neo4j.graphdb.Node chapterNode = graphDB.createNode(NodeLabels.CHAPTER);

                for (String name : SURA_NUMERIC_ATTRS) {
                    String temp = sura.getAttributes().getNamedItem(name).getNodeValue();
                    Integer value = Integer.parseInt(temp);
                    NodeUtils.setPropertyAndIndex(chapterNode, name, GraphIndices.ChapterIndex, value);
                }

                for (String name : SURA_STRING_ATTRS) {
                    String value = sura.getAttributes().getNamedItem(name).getNodeValue();
                    chapterNode.setProperty(name, value);
                    NodeUtils.setPropertyAndIndex(chapterNode, name, GraphIndices.ChapterIndex, value);
                }

            }
            tx.success();
        }
    }
}
