//package data.leeds.more;
//
//import base.GraphIndices;
//import base.NodeProperties;
//import data.DataFiller;
//import data.leeds.LeedsCorpusDataFiller;
//import model.api.chapter.Chapter;
//import model.api.verse.Verse;
//import model.api.word.Word;
//import model.impl.word.WordImpl;
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.Transaction;
//import util.NodeUtils;
//
//import java.util.List;
//
///**
// * Created by vahidoo on 3/18/15.
// */
//public class WordIndexInQuranDataFiller extends DataFiller {
//
//    public WordIndexInQuranDataFiller(GraphDatabaseService database) {
//        super(database);
//    }
//
//    @Override
//    protected List<Class<? extends DataFiller>> getDependencies() {
//        return getDependenciesList(LeedsCorpusDataFiller.class);
//    }
//
//    @Override
//    protected void internalFill() throws Throwable {
//        throw new RuntimeException();
//    }
//
//    @Override
//    public void fill() {
//
//        try (Transaction tx = database.beginTx()) {
//            int index = 1;
//
//            Node node = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, "1:1:1").getSingle();
//            Word current = new WordImpl(node);
//
//            while (current != null) {
//
//                node = ((WordImpl) current).getNode();
//                NodeUtils.setPropertyAndIndex(node, NodeProperties.Word.indexInQuran, GraphIndices.WordIndex, index);
//
//                System.out.println(current.getAddress() + "-->" + index);
//
//                current = nextWord(current);
//
//
//                index++;
//
//            }
//
//            tx.success();
//        }
//
//    }
//
//    private Word nextWord(Word current) {
//        Word successor = current.getSuccessor();
//        if (successor != null) {
//            return successor;
//        }
//
//        Verse nextVerse = current.getVerse().getSuccessor();
//
//        if (nextVerse != null)
//            return nextVerse.getWord(1);
//
//        Chapter nextChapter = current.getVerse().getChapter().getNextChapter();
//        if (nextChapter != null) {
//            return nextChapter.getVerse(1).getWord(1);
//        }
//
//
//        return null;
//    }
//
//}
