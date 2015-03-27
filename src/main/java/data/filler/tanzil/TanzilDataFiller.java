package data.filler.tanzil;

import data.schema.GraphIndices;
import data.schema.NodeLabels;
import data.schema.NodeProperties;
import data.schema.RelationshipTypes;
import data.filler.DataFiller;
import data.filler.TransactionalFiller;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
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
import java.util.*;

/**
 * Created by vahidoo on 3/5/15.
 */
public class TanzilDataFiller extends DataFiller {

    private static final String SURAS_TAG = "suras";
    private static final String SURA_TAG = "sura";
    private static final String AYA_TAG = "aya";


    private static final String[] SURA_NUMERIC_ATTRS = {NodeProperties.General.index, NodeProperties.Chapter.order, NodeProperties.Chapter.ayas};
    private static final String[] SURA_STRING_ATTRS = {NodeProperties.Chapter.type, NodeProperties.Chapter.name};

    private static final String TANZIL_UTHMANI_PATH_KEY = "tanzil.uthmani.path";
    private static final String TANZIL_METADATA_PATH_KEY = "tanzil.metadata.path";

    private File metadataFile;
    private File uthmaniFile;

    public TanzilDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database, managersSet, properties);

        logger.info((String) properties.get(TANZIL_UTHMANI_PATH_KEY));
        logger.info((String) properties.get(TANZIL_METADATA_PATH_KEY));

        uthmaniFile = new File((String) properties.get(TANZIL_UTHMANI_PATH_KEY));
        metadataFile = new File((String) properties.get(TANZIL_METADATA_PATH_KEY));
    }

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {

        List<TransactionalFiller> fillers = new ArrayList<>();

        fillers.add(new TanzilVerseFiller());
        fillers.add(new TanzilChapterFiller());

        return fillers;
    }


    private class TanzilVerseFiller implements TransactionalFiller {

        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(metadataFile);

            NodeList suras = ((Element) doc.getDocumentElement().getElementsByTagName(SURAS_TAG).item(0)).getElementsByTagName(SURA_TAG);

            for (int i = 0; i < suras.getLength(); i++) {
                System.out.println("add chapter #" + (i + 1));
                Node sura = suras.item(i);

                org.neo4j.graphdb.Node chapterNode = database.createNode(NodeLabels.CHAPTER);

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
        }
    }

    private class TanzilChapterFiller implements TransactionalFiller {
        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(uthmaniFile);

            NodeList suras = ((Element) doc.getDocumentElement()).getElementsByTagName(SURA_TAG);

            for (int i = 0; i < suras.getLength(); i++) {
                System.out.println("update chapter #" + (i + 1));
                Node sura = suras.item(i);

                int chapterIndex = Integer.parseInt(sura.getAttributes().getNamedItem(NodeProperties.General.index).getNodeValue());
                org.neo4j.graphdb.Node chapterNode = database.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.General.index, chapterIndex).getSingle();

                NodeList ayas = ((Element) sura).getElementsByTagName(AYA_TAG);
                for (int j = 0; j < ayas.getLength(); j++) {
                    Node aya = ayas.item(j);

                    int verseIndex = Integer.parseInt(aya.getAttributes().getNamedItem(NodeProperties.General.index).getNodeValue());
                    String text = aya.getAttributes().getNamedItem(NodeProperties.Verse.text).getNodeValue();
                    org.neo4j.graphdb.Node verseNode = database.createNode(NodeLabels.VERSE);
                    verseNode.setProperty(NodeProperties.General.index, verseIndex);
                    verseNode.setProperty(NodeProperties.Verse.text, text);

                    String address = NodeUtils.getNodeAddress(chapterIndex, verseIndex);
                    NodeUtils.setPropertyAndIndex(verseNode, NodeProperties.General.address, GraphIndices.VerseIndex, address);

                    chapterNode.createRelationshipTo(verseNode, RelationshipTypes.CONTAINS_VERSE);
                }
            }
        }
    }
}
