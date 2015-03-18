package model.impl.word;

import base.GraphIndices;
import base.NodeLabels;
import base.NodeProperties;
import base.RelationshipTypes;
import model.api.token.Token;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.NodeContainerImpl;
import model.impl.token.TokenImpl;
import model.impl.verse.VerseImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import util.NodeUtils;

import java.util.Iterator;

/**
 * Created by vahidoo on 3/13/15.
 */
public class WordImpl extends NodeContainerImpl implements Word {
    public WordImpl(Node node) {
        super(node);
    }

    @Override
    public Word getSuccessor() {
        Node n = NodeUtils.singleNeighborhood(node, RelationshipTypes.NEXT_WORD, Direction.OUTGOING, false);
        return (Word) NodeContainerImpl.createNewInstance(WordImpl.class, n);
    }

    @Override
    public Word getPredecessor() {
        Node n = NodeUtils.singleNeighborhood(node, RelationshipTypes.NEXT_WORD, Direction.INCOMING, true);
        return (Word) NodeContainerImpl.createNewInstance(WordImpl.class, n);
    }

    @Override
    public Token getStem() {
        Iterator<Relationship> it = node.getRelationships(Direction.OUTGOING, RelationshipTypes.CONTAINS_TOKEN).iterator();
        Node n = null;
        while (it.hasNext() && (n == null || !n.hasLabel(NodeLabels.STEM))) {
            n = it.next().getEndNode();
        }

        if (n == null) {
            throw new RuntimeException();
        }
        return new TokenImpl(n);
    }

    @Override
    public Verse getVerse() {
        Node n = NodeUtils.singleNeighborhood(node, RelationshipTypes.CONTAINS_WORD, Direction.INCOMING, true);
        return (Verse) NodeContainerImpl.createNewInstance(VerseImpl.class, n);
    }

    @Override
    public int getIndexInQuran() {
        return (int) node.getProperty(NodeProperties.Word.indexInQuran);

    }

    @Override
    public Word getSuccessorInQuran() {
//        Word successor = getSuccessor();
//        if (successor != null) {
//            return successor;
//        }
//
//        Verse nextVerse = getVerse().getSuccessor();
//
//        if ( nextVerse != null)
//            return nextVerse.getWord(1);
//
//        Chapter nextChapter = getVerse().getChapter().getNextChapter();
//        if ( nextChapter != null ){
//            return nextChapter.getVerse(1).getWord(1);
//        }
//
//        return null;

        int index = getIndexInQuran();
        Node next = node.getGraphDatabase().index().forNodes(GraphIndices.WordIndex).get(NodeProperties.Word.indexInQuran, index + 1).getSingle();
        if (next == null)
            return null;

        return new WordImpl(next);

    }

    @Override
    public String getAddress() {
        return (String) node.getProperty(NodeProperties.General.address);
    }

}
