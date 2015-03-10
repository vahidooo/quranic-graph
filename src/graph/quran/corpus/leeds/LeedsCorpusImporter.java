package graph.quran.corpus.leeds;

import base.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import util.NodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vahidoo on 3/5/15.
 */
public class LeedsCorpusImporter implements Importer {

    private static final int IGNORED_LINES = 57;
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


    private Map<String, Object> prev, current;

    public LeedsCorpusImporter(String filename) {
        corpusFile = new File(filename);

        prev = new HashMap<>(4);
        prev.put(CHAPTER, 0);
        prev.put(VERSE, 0);
        prev.put(WORD, 0);
        prev.put(TOKEN, 0);

        current = new HashMap<>(4);
        current.put(CHAPTER, 0);

    }


    private void doImport(GraphDatabaseService graphDB, Scanner scanner) throws FileNotFoundException {
        try (Transaction tx = graphDB.beginTx()) {
            int counter = 0;
            while (scanner.hasNextLine()) {
                System.out.println( "add token #" + (++counter) );
                String line = scanner.nextLine();

                String[] options = line.split("\t");
                if (options.length != 4)
                    throw new RuntimeException();

                Node tokenNode = processAddressAndForm(graphDB, options[0], options[1]);
                processTag(graphDB, tokenNode, options[2]);
                processFeatures(graphDB, tokenNode, options[3]);
            }
            tx.success();
        }

    }

    public void doImport(GraphDatabaseService graphDB, int tokensPerTransaction) throws FileNotFoundException {


        Scanner scanner = new Scanner(corpusFile);

        for (int i = 0; i < IGNORED_LINES; i++) {
            scanner.nextLine();
        }

        StringBuilder sb = new StringBuilder();
        Scanner tempScanner;
        int counter = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            counter++;
            sb.append(line);
            sb.append("\n");

            if (counter == tokensPerTransaction) {
                tempScanner = new Scanner(sb.toString());
                doImport(graphDB, tempScanner);
                sb = new StringBuilder();
            }
        }

        tempScanner = new Scanner(sb.toString());
        doImport(graphDB, tempScanner);

    }

    @Override
    public void doImport(GraphDatabaseService graphDB) throws FileNotFoundException {

        Scanner scanner = new Scanner(corpusFile);

        for (int i = 0; i < 57; i++) {
            scanner.nextLine();
        }

        doImport(graphDB, scanner);
    }


    private void processFeatures(GraphDatabaseService graphDB, Node tokenNode, String options) {
        String[] features = options.split("\\|");

        NodeLabels label = NodeLabels.valueOf(features[0]);
        tokenNode.addLabel(label);
        List<String> list = new ArrayList<>();

        if (label.equals(NodeLabels.STEM)) {
            for (String feature : features) {
                if (feature.startsWith("ROOT")) {
                    createIfAbsentRoot(graphDB, tokenNode, feature.split(":")[1]);
                } else if (feature.startsWith("LEM")) {
                    createIfAbsentLemma(graphDB, tokenNode, feature.split(":")[1]);
                } else if (feature.startsWith("POS")) {
                    createIfAbsentPos(graphDB, tokenNode, feature.split(":")[1]);
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


    private void createIfAbsentRoot(GraphDatabaseService graphDB, Node node, String root) {

        Node rootNode = graphDB.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.buckwalter, root).getSingle();

        if (rootNode == null) {
            rootNode = graphDB.createNode(NodeLabels.ROOT);
            NodeUtils.setBuckwalterPropertyAndIndex(rootNode, GraphIndices.RootIndex, root);

        }

        node.createRelationshipTo(rootNode, RelationshipTypes.HAS_ROOT);
    }

    private void createIfAbsentLemma(GraphDatabaseService graphDB, Node node, String lemma) {

        Node lemmaNode = graphDB.index().forNodes(GraphIndices.LemmaIndex).get(NodeProperties.GeneralText.buckwalter, lemma).getSingle();

        if (lemmaNode == null) {
            lemmaNode = graphDB.createNode(NodeLabels.LEMMA);
            NodeUtils.setBuckwalterPropertyAndIndex(lemmaNode, GraphIndices.LemmaIndex, lemma);

        }

        node.createRelationshipTo(lemmaNode, RelationshipTypes.HAS_LEMMA);
    }

    private void createIfAbsentPos(GraphDatabaseService graphDB, Node node, String pos) {

        Node posNode = graphDB.index().forNodes(GraphIndices.PosIndex).get(NodeProperties.General.value, pos).getSingle();

        if (posNode == null) {
            posNode = graphDB.createNode(NodeLabels.POS);
            NodeUtils.setPropertyAndIndex(posNode, NodeProperties.General.value, GraphIndices.PosIndex, pos);

        }

        node.createRelationshipTo(posNode, RelationshipTypes.HAS_POS);
    }


    private void processTag(GraphDatabaseService graphDB, Node tokenNode, String tag) {

        Node tagNode = graphDB.index().forNodes(GraphIndices.TagIndex).get(NodeProperties.General.value, tag).getSingle();
        if (tagNode == null) {
            tagNode = graphDB.createNode(NodeLabels.TAG);
            NodeUtils.setPropertyAndIndex(tagNode, NodeProperties.General.value, GraphIndices.TagIndex, tag);
        }

        tokenNode.createRelationshipTo(tagNode, RelationshipTypes.HAS_TAG);
    }

    private Node processAddressAndForm(GraphDatabaseService graphDB, String addr, String form) {

        Matcher matcher = addressPattern.matcher(addr);
        if (!matcher.matches()) {
            throw new RuntimeException();
        }

        Integer chapter = Integer.parseInt(matcher.group(1));
        Integer verse = Integer.parseInt(matcher.group(2));
        Integer word = Integer.parseInt(matcher.group(3));
        Integer token = Integer.parseInt(matcher.group(4));

        current.put(CHAPTER, chapter);
        current.put(VERSE, verse);
        current.put(WORD, word);
        current.put(TOKEN, token);

        Node chapterNode = graphDB.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.General.index, chapter).getSingle();
        Node verseNode = createIfAbsentVerse(graphDB, chapterNode, chapter, verse);
        Node wordNode = createIfAbsentWord(graphDB, verseNode, word);
        Node tokenNode = createToken(graphDB, wordNode, token, form);

        prev.put(CHAPTER, current.get(CHAPTER));
        prev.put(VERSE, current.get(VERSE));
        prev.put(WORD, current.get(WORD));
        prev.put(TOKEN, current.get(TOKEN));

        return tokenNode;
    }

    private Node createToken(GraphDatabaseService graphDB, Node wordNode, Integer index, String form) {
        Node tokenNode = graphDB.createNode(NodeLabels.TOKEN);
        tokenNode.setProperty(NodeProperties.General.index, index);

        NodeUtils.setBuckwalterPropertyAndIndex(tokenNode, GraphIndices.TokenIndex, form);

        wordNode.createRelationshipTo(tokenNode, RelationshipTypes.CONTAINS_TOKEN);
        return tokenNode;
    }


    private Node createIfAbsentWord(GraphDatabaseService graphDB, Node verseNode, Integer index) {

        Node wordNode;
        if (prev.get(VERSE) != current.get(VERSE) || prev.get(WORD) != current.get(WORD)) {
            wordNode = graphDB.createNode(NodeLabels.WORD);
            wordNode.setProperty(NodeProperties.General.index, index);
            current.put(WORD_NODE, wordNode);

            verseNode.createRelationshipTo(wordNode, RelationshipTypes.CONTAINS_WORD);
        } else {
            wordNode = (Node) current.get(WORD_NODE);
        }
        return wordNode;
    }

    private Node createIfAbsentVerse(GraphDatabaseService graphDB, Node chapterNode, Integer chapterIndex, Integer verseIndex) {

        Node verseNode = graphDB.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getVerseAddress(chapterIndex, verseIndex)).getSingle();

        if (verseNode != null)
            return verseNode;


        if (prev.get(VERSE) != current.get(VERSE)) {
            verseNode = graphDB.createNode(NodeLabels.VERSE);
            verseNode.setProperty(NodeProperties.General.index, verseIndex);
            current.put(VERSE_NODE, verseNode);

            chapterNode.createRelationshipTo(verseNode, RelationshipTypes.CONTAINS_VERSE);
        } else {
            verseNode = (Node) current.get(VERSE_NODE);
        }
        return verseNode;
    }
}



