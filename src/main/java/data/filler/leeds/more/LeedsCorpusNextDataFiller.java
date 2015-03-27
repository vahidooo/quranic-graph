package data.filler.leeds.more;

import data.schema.GraphIndices;
import data.schema.NodeProperties;
import data.schema.RelationshipTypes;
import data.filler.DataFiller;
import data.filler.TransactionalFiller;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by vahidoo on 3/13/15.
 */
public class LeedsCorpusNextDataFiller extends DataFiller {

    public LeedsCorpusNextDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database,managersSet, properties);
    }

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {
        List<TransactionalFiller> fillers = new ArrayList<>();

        for (int i = 1; i < 115; i++) {
            fillers.add(new VerseUpdater(i));
            fillers.add(new WordUpdater(i));
        }


        return fillers;
    }

    private class VerseUpdater implements TransactionalFiller {

        private int chapter;

        public VerseUpdater(int chapter) {
            this.chapter = chapter;
        }

        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {

            int verse = 1;

            Node current = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
            Node next = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse + 1)).getSingle();

            while (next != null) {
                current.createRelationshipTo(next, RelationshipTypes.NEXT_VERSE);
                System.out.println("add relationship :" + current.getProperty(NodeProperties.General.address) + " -NEXT_VERSE-> " + next.getProperty(NodeProperties.General.address));

                verse++;
                current = next;
                next = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse + 1)).getSingle();
            }
        }
    }

    private class WordUpdater implements TransactionalFiller {

        private int chapter;

        public WordUpdater(int chapter) {
            this.chapter = chapter;
        }

        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {
            int verse = 1;

            Node verseNode = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
            while (verseNode != null) {

                int word = 1;

                String verseAddr = (String) verseNode.getProperty(NodeProperties.General.address);
                Node current = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word)).getSingle();
                Node next = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word + 1)).getSingle();

                while (next != null) {
                    current.createRelationshipTo(next, RelationshipTypes.NEXT_WORD);
                    System.out.println("add relationship :" + current.getProperty(NodeProperties.General.address) + " -NEXT_WORD-> " + next.getProperty(NodeProperties.General.address));

                    word++;
                    current = next;
                    next = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(verseAddr, word + 1)).getSingle();
                }
                verse++;

                verseNode = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
            }
        }
    }
}
