package model.impl.token;

import base.NodeLabels;
import base.NodeProperties;
import base.RelationshipTypes;
import model.api.root.Root;
import model.api.token.Token;
import model.api.token.TokenPosition;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.NodeContainerImpl;
import model.impl.base.TextualImpl;
import model.impl.root.RootImpl;
import model.impl.word.WordImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import util.NodeUtils;

/**
 * Created by vahidoo on 3/13/15.
 */
public class TokenImpl extends TextualImpl implements Token {
    public TokenImpl(Node node) {
        super(node);
    }

    @Override
    public Word getWord() {
        Node word = NodeUtils.singleNeighborhood(node, RelationshipTypes.CONTAINS_TOKEN, Direction.INCOMING, true);
        return (Word) NodeContainerImpl.createNewInstance(WordImpl.class, word);
    }

    @Override
    public boolean hasRoot() {
        return getRoot() != null;
    }

    @Override
    public Root getRoot() {
        Node root = NodeUtils.singleNeighborhood(node, RelationshipTypes.HAS_ROOT, Direction.OUTGOING, false);
        return (Root) NodeContainerImpl.createNewInstance(RootImpl.class, root);
    }

    @Override
    public TokenPosition getPosition() {
        if (node.hasLabel(NodeLabels.PREFIX))
            return TokenPosition.PREFIX;
        if (node.hasLabel(NodeLabels.SUFFIX))
            return TokenPosition.SUFFIX;
        if (node.hasLabel(NodeLabels.STEM))
            return TokenPosition.STEM;

        throw new RuntimeException();
    }

    @Override
    public Integer getIndex() {
        return (Integer) node.getProperty(NodeProperties.General.index);
    }

    @Override
    public Verse getVerse() {
        return getWord().getVerse();
    }

}
