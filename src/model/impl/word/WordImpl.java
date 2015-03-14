package model.impl.word;

import base.NodeLabels;
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
}
