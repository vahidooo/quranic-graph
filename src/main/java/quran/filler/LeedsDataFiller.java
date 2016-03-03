package quran.filler;

import org.jqurantree.arabic.ArabicText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;
import quran.entity.*;
import quran.repository.VerseRepository;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vahidoo on 3/5/15.
 */

public class LeedsDataFiller implements DataFiller {

    private static final String ROOT = "ROOT";
    private static final String LEMMA = "LEM";
    private static final String POS = "POS";
    private int ignoredLines;
    private InputStream corpusFile;

    @Autowired
    private Neo4jOperations template;
    @Autowired
    private VerseRepository verseRepository;

    private Map<String, Root> roots = new HashMap<>();
    private Map<String, Lemma> lemmas = new HashMap<>();

    private static final Pattern addressPattern = Pattern.compile("\\((\\d+):(\\d+):(\\d+):(\\d+)\\)");

    public void setIgnoredLines(int ignoredLines) {
        this.ignoredLines = ignoredLines;
    }

    public void setCorpusFile(InputStream corpusFile) {
        this.corpusFile = corpusFile;
    }

    @Override
    @Transactional
    public void fill() throws ParserConfigurationException, IOException, SAXException {

        Address currentAddr;
        Address prevAddr = new Address("(0:0:0:0)");
        Word currentWord = null;

        Scanner scanner = new Scanner(corpusFile);
        for (int i = 0; i < ignoredLines; i++) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] options = line.split("\t");
            if (options.length != 4) {
                throw new RuntimeException(line);
            }

            currentAddr = new Address(options[0]);
            System.out.println(options[0]);

            if (prevAddr.word != currentAddr.word) {
                currentWord = addWord(currentAddr, options);
            } else {

            }
            String[] features = options[3].split("\\|");
            Token currentToken = addToken(currentAddr, currentWord, options[1], options[2], features);

            if (currentToken.getAffix().equals(Token.TokenAffix.STEM)) {
                currentWord.setStem(currentToken);
            }

            prevAddr = currentAddr;

            template.save(currentToken);
            template.save(currentWord);
        }

    }

    private Word addWord(Address addr, String[] options) {
        Word word = new Word();
        Verse verse = verseRepository.findByChapterAndIndex(addr.chapter, addr.verse);
        word.setVerse(verse);
        word.setIndex(addr.word);
        word.setAddress(String.format("%s%s%d", verse.getAddress(), TanzilDataFiller.ADDRESS_SEPARATOR, word.getIndex()));
        return word;
    }

    private Token addToken(Address addr, Word word, String buckwalter, String tag, String[] features) {
        Token token = new Token();
        token.setIndex(addr.token);
        token.setWord(word);
        token.setTag(tag);

        ArabicText arabic = ArabicText.fromBuckwalter(buckwalter);
        ArabicText simpleArabic = arabic.removeDiacritics();

        token.setForm(arabic.toString());
        token.setSimple(simpleArabic.toString());


        Token.TokenAffix affix = Token.TokenAffix.valueOf(features[0]);
        token.setAffix(affix);
        token.setAddress(String.format("%s%s%d", word.getAddress(), TanzilDataFiller.ADDRESS_SEPARATOR, token.getIndex()));

        addFeatures(token, features);

        return token;
    }

    private void addFeatures(Token token, String[] features) {


        if (token.getAffix().equals(Token.TokenAffix.STEM)) {
            for (String feature : features) {
                if (feature.startsWith(ROOT)) {
                    Root root = addIfAbsentRoot(feature.split(":")[1]);
                    token.setRoot(root);
                } else if (feature.startsWith(LEMMA)) {
                    Lemma lemma = addIfAbsentLemma(feature.split(":")[1]);
                    token.setLemma(lemma);
//                } else if (feature.startsWith(POS)) {
//                    addIfAbsentPOS(feature.split(":")[1]);
                }
                //TODO
            }

        } else if (token.getAffix().equals(Token.TokenAffix.SUFFIX)) {
            //TODO
        } else if (token.getAffix().equals(Token.TokenAffix.PREFIX)) {
            //TODO
        }

    }

    private Root addIfAbsentRoot(String r) {
        if (!roots.containsKey(r)) {
            Root root = new Root();

            ArabicText arabic = ArabicText.fromBuckwalter(r);
            root.setForm(arabic.toString());
            roots.put(r, root);
            template.save(root);
        }
        return roots.get(r);
    }

    private Lemma addIfAbsentLemma(String l) {
        if (!lemmas.containsKey(l)) {
            Lemma lemma = new Lemma();

            ArabicText arabic = ArabicText.fromBuckwalter(l);
            ArabicText simpleArabic = arabic.removeDiacritics();

            lemma.setForm(arabic.toString());
            lemma.setSimple(simpleArabic.toString());

            lemmas.put(l, lemma);
            template.save(lemma);
        }
        return lemmas.get(l);
    }

    //
//    private void createIfAbsentRoot(GraphDatabaseService database, Node node, String root) {
//
//        Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.buckwalter, root).getSingle();
//
//        if (rootNode == null) {
//            rootNode = database.createNode(NodeLabels.ROOT);
//            NodeUtils.setBuckwalterPropertyAndIndex(rootNode, GraphIndices.RootIndex, root);
//
//        }
//
//        node.createRelationshipTo(rootNode, RelationshipTypes.HAS_ROOT);
//    }
//
//    private void createIfAbsentLemma(GraphDatabaseService database, Node node, String lemma) {
//
//        Node lemmaNode = database.index().forNodes(GraphIndices.LemmaIndex).get(NodeProperties.GeneralText.buckwalter, lemma).getSingle();
//
//        if (lemmaNode == null) {
//            lemmaNode = database.createNode(NodeLabels.LEMMA);
//            NodeUtils.setBuckwalterPropertyAndIndex(lemmaNode, GraphIndices.LemmaIndex, lemma);
//
//        }
//
//        node.createRelationshipTo(lemmaNode, RelationshipTypes.HAS_LEMMA);
//    }
//
//    private void createIfAbsentPos(GraphDatabaseService database, Node node, String pos) {
//
//        Node posNode = database.index().forNodes(GraphIndices.PosIndex).get(NodeProperties.General.value, pos).getSingle();
//
//        if (posNode == null) {
//            posNode = database.createNode(NodeLabels.POS);
//            NodeUtils.setPropertyAndIndex(posNode, NodeProperties.General.value, GraphIndices.PosIndex, pos);
//
//        }
//
//        node.createRelationshipTo(posNode, RelationshipTypes.HAS_POS);
//    }
//
//
//    private void processTag(GraphDatabaseService database, Node tokenNode, String tag) {
//
//        Node tagNode = database.index().forNodes(GraphIndices.TagIndex).get(NodeProperties.General.value, tag).getSingle();
//        if (tagNode == null) {
//            tagNode = database.createNode(NodeLabels.TAG);
//            NodeUtils.setPropertyAndIndex(tagNode, NodeProperties.General.value, GraphIndices.TagIndex, tag);
//        }
//
//        tokenNode.createRelationshipTo(tagNode, RelationshipTypes.HAS_TAG);
//    }
//
//    private Node processAddressAndForm(GraphDatabaseService database, String addr, String form) {
//
//        Matcher matcher = addressPattern.matcher(addr);
//        if (!matcher.matches()) {
//            throw new RuntimeException();
//        }
//
//        Integer chapter = Integer.parseInt(matcher.group(1));
//        Integer verse = Integer.parseInt(matcher.group(2));
//        Integer word = Integer.parseInt(matcher.group(3));
//        Integer token = Integer.parseInt(matcher.group(4));
//
////        current.put(CHAPTER, chapter);
////        current.put(VERSE, verse);
////        current.put(WORD, word);
////        current.put(TOKEN, token);
//
//        Node chapterNode = database.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.General.index, chapter).getSingle();
//        Node verseNode = createIfAbsentVerse(database, chapterNode, chapter, verse);
//        Node wordNode = createIfAbsentWord(database, verseNode, word);
//        Node tokenNode = createToken(database, wordNode, token, form);
//
////        prev.put(CHAPTER, current.get(CHAPTER));
////        prev.put(VERSE, current.get(VERSE));
////        prev.put(WORD, current.get(WORD));
////        prev.put(TOKEN, current.get(TOKEN));
//
//        return tokenNode;
//    }
//
//    private Node createToken(GraphDatabaseService database, Node wordNode, Integer index, String form) {
//        Node tokenNode = database.createNode(NodeLabels.TOKEN);
//        tokenNode.setProperty(NodeProperties.General.index, index);
//
//        NodeUtils.setBuckwalterPropertyAndIndex(tokenNode, GraphIndices.TokenIndex, form);
//
//        String address = NodeUtils.getNodeAddress((String) wordNode.getProperty(NodeProperties.General.address), index);
//        NodeUtils.setPropertyAndIndex(tokenNode, NodeProperties.General.address, GraphIndices.TokenIndex, address);
//
//        wordNode.createRelationshipTo(tokenNode, RelationshipTypes.CONTAINS_TOKEN);
//        return tokenNode;
//    }
//
//
//    private Node createIfAbsentWord(GraphDatabaseService database, Node verseNode, Integer index) {
//
//        Node wordNode;
////        if (prev.get(VERSE) != current.get(VERSE) || prev.get(WORD) != current.get(WORD)) {
//        String address = NodeUtils.getNodeAddress((String) verseNode.getProperty(NodeProperties.General.address), index);
//        wordNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, address).getSingle();
//        if (wordNode == null) {
//            wordNode = database.createNode(NodeLabels.WORD);
//            wordNode.setProperty(NodeProperties.General.index, index);
//
//            NodeUtils.setPropertyAndIndex(wordNode, NodeProperties.General.address, GraphIndices.WordIndex, address);
//
////            current.put(WORD_NODE, wordNode);
//
//            verseNode.createRelationshipTo(wordNode, RelationshipTypes.CONTAINS_WORD);
//        }
////        else {
////            wordNode = (Node) current.get(WORD_NODE);
////        }
//        return wordNode;
//    }
//
//    private Node createIfAbsentVerse(GraphDatabaseService database, Node chapterNode, Integer chapterIndex, Integer verseIndex) {
//
//        Node verseNode = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(chapterIndex, verseIndex)).getSingle();
//
//        if (verseNode != null)
//            return verseNode;
//
//
////        if (prev.get(VERSE) != current.get(VERSE)) {
//
//
//        String address = NodeUtils.getNodeAddress(chapterIndex, verseIndex);
//        verseNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, address).getSingle();
//        if (verseNode == null) {
//            verseNode = database.createNode(NodeLabels.VERSE);
//            verseNode.setProperty(NodeProperties.General.index, verseIndex);
//
//            NodeUtils.setPropertyAndIndex(verseNode, NodeProperties.General.address, GraphIndices.VerseIndex, address);
//
////            current.put(VERSE_NODE, verseNode);
//
//            chapterNode.createRelationshipTo(verseNode, RelationshipTypes.CONTAINS_VERSE);
//        }
////        else {
////            verseNode = (Node) current.get(VERSE_NODE);
////        }
//        return verseNode;
//    }
//
//
    class Address {
        private int chapter;
        private int verse;
        private int word;
        private int token;

        public Address(String addr) {
            Matcher matcher = addressPattern.matcher(addr);
            if (!matcher.matches()) {
                throw new RuntimeException(addr);
            }

            chapter = Integer.parseInt(matcher.group(1));
            verse = Integer.parseInt(matcher.group(2));
            word = Integer.parseInt(matcher.group(3));
            token = Integer.parseInt(matcher.group(4));

        }


    }

}



