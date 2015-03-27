package data.leeds.more;

import base.GraphIndices;
import base.NodeProperties;
import data.DataFiller;
import data.TransactionalFiller;
import model.api.verse.Verse;
import model.api.verse.VerseManager;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by vahidoo on 3/18/15.
 */
public class NextVerseInQuranDataFiller extends DataFiller {

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {
        List<TransactionalFiller> fillers = new ArrayList<>();
        fillers.add(new NextVerseUpdater());
        return fillers;
    }

    public NextVerseInQuranDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database, managersSet, properties);
    }


    private class NextVerseUpdater implements TransactionalFiller {
        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {

            int index = 1;
            VerseManager verseManager = managersSet.getVerseManager();
            Verse current = verseManager.get(1, 1);

            while (current != null) {

                node = current.getNode();
                NodeUtils.setPropertyAndIndex(node, NodeProperties.Verse.indexInQuran, GraphIndices.VerseIndex, index);

                current = nextVerse(current, verseManager);

                index++;
                logger.info("index verse #" + index);
            }
        }

        private Verse nextVerse(Verse verse, VerseManager verseManager) {
            Verse successor = verse.getSuccessor();

            if (successor != null) {
                return successor;
            }

            int chapter = verse.getChapter().getIndex();
            if (chapter == 114) {
                return null;
            }

            return verseManager.get(chapter + 1, 1);
        }
    }

}