package graph.quran.corpus.leeds;

import base.GraphIndices;
import base.Importer;
import base.NodeProperties;
import base.RelationshipTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 3/13/15.
 */
public class LeedsCorpusNextUpdater implements Importer {
    @Override
    public void doImport(GraphDatabaseService graphDB) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("add NEXT_* relationships");
        updateVerses(graphDB);
        updateWords(graphDB);


    }

    private void updateWords(GraphDatabaseService graphDB) {

        try (Transaction tx = graphDB.beginTx()) {

            for (int chapter = 1; chapter < 115; chapter++) {

                int verse = 1;

                Node verseNode = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
                while (verseNode != null) {

                    int word = 1;

                    String verseAddr = (String) verseNode.getProperty(NodeProperties.General.address);
                    Node current = graphDB.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word)).getSingle();
                    Node next = graphDB.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word + 1)).getSingle();

                    while (next != null) {
                        current.createRelationshipTo(next, RelationshipTypes.NEXT_WORD);
                        System.out.println("add relationship :" + current.getProperty(NodeProperties.General.address) + " -NEXT_WORD-> " + next.getProperty(NodeProperties.General.address));

                        word++;
                        current = next;
                        next = graphDB.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word + 1)).getSingle();
                    }
                    verse++;

                    verseNode = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
                }


            }
            tx.success();
        }
    }

    private void updateVerses(GraphDatabaseService graphDB) {

        try (Transaction tx = graphDB.beginTx()) {

            for (int chapter = 1; chapter < 115; chapter++) {

                int verse = 1;

                Node current = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
                Node next = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse + 1)).getSingle();

                while (next != null) {
                    current.createRelationshipTo(next, RelationshipTypes.NEXT_VERSE);
                    System.out.println("add relationship :" + current.getProperty(NodeProperties.General.address) + " -NEXT_VERSE-> " + next.getProperty(NodeProperties.General.address));

                    verse++;
                    current = next;
                    next = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse + 1)).getSingle();
                }
            }
            tx.success();
        }
    }

}
