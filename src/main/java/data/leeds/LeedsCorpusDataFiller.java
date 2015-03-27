package data.leeds;

import base.GraphIndices;
import base.NodeLabels;
import base.NodeProperties;
import base.RelationshipTypes;
import data.DataFiller;
import data.TransactionalFiller;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.xml.sax.SAXException;
import util.NodeUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vahidoo on 3/5/15.
 */

public class LeedsCorpusDataFiller extends DataFiller {

    private static final String LEEDS_CORPUS_PATH_KEY = "leeds.corpus.path";
    private static final String LEEDS_CORPUS_TOKEN_PER_TRANSACTION_KEY = "leeds.corpus.tokenPerTransaction";
    private static final String LEEDS_CORPUS_IGNORED_LINES_KEY = "leeds.corpus.ignoredLines";


    private File corpusFile;

    private static final Pattern addressPattern = Pattern.compile("\\((\\d+):(\\d+):(\\d+):(\\d+)\\)");

    private static final String CHAPTER = "CH";
    private static final String VERSE = "V";
    private static final String WORD = "W";
    private static final String TOKEN = "T";

    private static final String CHAPTER_NODE = "CH_NODE";
    private static final String VERSE_NODE = "V_NODE";
    private static final String WORD_NODE = "W_NODE";
    private static final String TOKEN_NODE = "T_NODE";

    public LeedsCorpusDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database,managersSet, properties);
        corpusFile = new File((String) properties.get(LEEDS_CORPUS_PATH_KEY));
    }

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {
        List<TransactionalFiller> fillers = new ArrayList<>();

        List<Scanner> chunks = splitCorpus();

        for (Scanner chunk : chunks) {
            CorpusFiller filler = new CorpusFiller(chunk);
            fillers.add(filler);
        }

        return fillers;
    }

    public List<Scanner> splitCorpus() throws FileNotFoundException {

        Scanner scanner = new Scanner(corpusFile);
        List<Scanner> chunks = new ArrayList<>();

        int ignoredLines = Integer.parseInt((String) properties.get(LEEDS_CORPUS_IGNORED_LINES_KEY));
        int tokensPerTransaction = Integer.parseInt((String) properties.get(LEEDS_CORPUS_TOKEN_PER_TRANSACTION_KEY));

        for (int i = 0; i < ignoredLines; i++) {
            scanner.nextLine();
        }

        StringBuilder sb = new StringBuilder();
        Scanner chunk;
        int counter = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            counter++;
            sb.append(line);
            sb.append("\n");

            if (counter % tokensPerTransaction == 0) {
                System.out.println("TRANSACTION#" + (counter / tokensPerTransaction));
                chunk = new Scanner(sb.toString());
                chunks.add(chunk);
                sb = new StringBuilder();
            }
        }

        chunk = new Scanner(sb.toString());
        chunks.add(chunk);

        return chunks;
    }

//    public void doImport(GraphDatabaseService database) throws FileNotFoundException {
//
//        Scanner scanner = new Scanner(corpusFile);
//
//        for (int i = 0; i < 57; i++) {
//            scanner.nextLine();
//        }
//
//        doImport(scanner);
//    }


    private class CorpusFiller implements TransactionalFiller {
        Scanner scanner;

        public CorpusFiller(Scanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] options = line.split("\t");
                if (options.length != 4)
                    throw new RuntimeException();

                Node tokenNode = processAddressAndForm(database, options[0], options[1]);
                processTag(database, tokenNode, options[2]);
                processFeatures(database, tokenNode, options[3]);
            }

        }

        private void processFeatures(GraphDatabaseService database, Node tokenNode, String options) {
            String[] features = options.split("\\|");

            NodeLabels label = NodeLabels.valueOf(features[0]);
            tokenNode.addLabel(label);
            List<String> list = new ArrayList<>();

            if (label.equals(NodeLabels.STEM)) {
                for (String feature : features) {
                    if (feature.startsWith("ROOT")) {
                        createIfAbsentRoot(database, tokenNode, feature.split(":")[1]);
                    } else if (feature.startsWith("LEM")) {
                        createIfAbsentLemma(database, tokenNode, feature.split(":")[1]);
                    } else if (feature.startsWith("POS")) {
                        createIfAbsentPos(database, tokenNode, feature.split(":")[1]);
                    }
                    //TODO
                }

            } else if (label.equals(NodeLabels.SUFFIX)) {
                //TODO
            }
            if (label.equals(NodeLabels.PREFIX)) {
                //TODO
            }

        }

        private void createIfAbsentRoot(GraphDatabaseService database, Node node, String root) {

            Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.buckwalter, root).getSingle();

            if (rootNode == null) {
                rootNode = database.createNode(NodeLabels.ROOT);
                NodeUtils.setBuckwalterPropertyAndIndex(rootNode, GraphIndices.RootIndex, root);

            }

            node.createRelationshipTo(rootNode, RelationshipTypes.HAS_ROOT);
        }

        private void createIfAbsentLemma(GraphDatabaseService database, Node node, String lemma) {

            Node lemmaNode = database.index().forNodes(GraphIndices.LemmaIndex).get(NodeProperties.GeneralText.buckwalter, lemma).getSingle();

            if (lemmaNode == null) {
                lemmaNode = database.createNode(NodeLabels.LEMMA);
                NodeUtils.setBuckwalterPropertyAndIndex(lemmaNode, GraphIndices.LemmaIndex, lemma);

            }

            node.createRelationshipTo(lemmaNode, RelationshipTypes.HAS_LEMMA);
        }

        private void createIfAbsentPos(GraphDatabaseService database, Node node, String pos) {

            Node posNode = database.index().forNodes(GraphIndices.PosIndex).get(NodeProperties.General.value, pos).getSingle();

            if (posNode == null) {
                posNode = database.createNode(NodeLabels.POS);
                NodeUtils.setPropertyAndIndex(posNode, NodeProperties.General.value, GraphIndices.PosIndex, pos);

            }

            node.createRelationshipTo(posNode, RelationshipTypes.HAS_POS);
        }


        private void processTag(GraphDatabaseService database, Node tokenNode, String tag) {

            Node tagNode = database.index().forNodes(GraphIndices.TagIndex).get(NodeProperties.General.value, tag).getSingle();
            if (tagNode == null) {
                tagNode = database.createNode(NodeLabels.TAG);
                NodeUtils.setPropertyAndIndex(tagNode, NodeProperties.General.value, GraphIndices.TagIndex, tag);
            }

            tokenNode.createRelationshipTo(tagNode, RelationshipTypes.HAS_TAG);
        }

        private Node processAddressAndForm(GraphDatabaseService database, String addr, String form) {

            Matcher matcher = addressPattern.matcher(addr);
            if (!matcher.matches()) {
                throw new RuntimeException();
            }

            Integer chapter = Integer.parseInt(matcher.group(1));
            Integer verse = Integer.parseInt(matcher.group(2));
            Integer word = Integer.parseInt(matcher.group(3));
            Integer token = Integer.parseInt(matcher.group(4));

//        current.put(CHAPTER, chapter);
//        current.put(VERSE, verse);
//        current.put(WORD, word);
//        current.put(TOKEN, token);

            Node chapterNode = database.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.General.index, chapter).getSingle();
            Node verseNode = createIfAbsentVerse(database, chapterNode, chapter, verse);
            Node wordNode = createIfAbsentWord(database, verseNode, word);
            Node tokenNode = createToken(database, wordNode, token, form);

//        prev.put(CHAPTER, current.get(CHAPTER));
//        prev.put(VERSE, current.get(VERSE));
//        prev.put(WORD, current.get(WORD));
//        prev.put(TOKEN, current.get(TOKEN));

            return tokenNode;
        }

        private Node createToken(GraphDatabaseService database, Node wordNode, Integer index, String form) {
            Node tokenNode = database.createNode(NodeLabels.TOKEN);
            tokenNode.setProperty(NodeProperties.General.index, index);

            NodeUtils.setBuckwalterPropertyAndIndex(tokenNode, GraphIndices.TokenIndex, form);

            String address = NodeUtils.getNodeAddress((String) wordNode.getProperty(NodeProperties.General.address), index);
            NodeUtils.setPropertyAndIndex(tokenNode, NodeProperties.General.address, GraphIndices.TokenIndex, address);

            wordNode.createRelationshipTo(tokenNode, RelationshipTypes.CONTAINS_TOKEN);
            return tokenNode;
        }


        private Node createIfAbsentWord(GraphDatabaseService database, Node verseNode, Integer index) {

            Node wordNode;
//        if (prev.get(VERSE) != current.get(VERSE) || prev.get(WORD) != current.get(WORD)) {
            String address = NodeUtils.getNodeAddress((String) verseNode.getProperty(NodeProperties.General.address), index);
            wordNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, address).getSingle();
            if (wordNode == null) {
                wordNode = database.createNode(NodeLabels.WORD);
                wordNode.setProperty(NodeProperties.General.index, index);

                NodeUtils.setPropertyAndIndex(wordNode, NodeProperties.General.address, GraphIndices.WordIndex, address);

//            current.put(WORD_NODE, wordNode);

                verseNode.createRelationshipTo(wordNode, RelationshipTypes.CONTAINS_WORD);
            }
//        else {
//            wordNode = (Node) current.get(WORD_NODE);
//        }
            return wordNode;
        }

        private Node createIfAbsentVerse(GraphDatabaseService database, Node chapterNode, Integer chapterIndex, Integer verseIndex) {

            Node verseNode = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapterIndex, verseIndex)).getSingle();

            if (verseNode != null)
                return verseNode;


//        if (prev.get(VERSE) != current.get(VERSE)) {


            String address = NodeUtils.getNodeAddress(chapterIndex, verseIndex);
            verseNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, address).getSingle();
            if (verseNode == null) {
                verseNode = database.createNode(NodeLabels.VERSE);
                verseNode.setProperty(NodeProperties.General.index, verseIndex);

                NodeUtils.setPropertyAndIndex(verseNode, NodeProperties.General.address, GraphIndices.VerseIndex, address);

//            current.put(VERSE_NODE, verseNode);

                chapterNode.createRelationshipTo(verseNode, RelationshipTypes.CONTAINS_VERSE);
            }
//        else {
//            verseNode = (Node) current.get(VERSE_NODE);
//        }
            return verseNode;
        }

    }
}



