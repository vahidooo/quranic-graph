package model.impl.token;

import data.schema.NodeLabels;
import data.schema.NodeProperties;
import data.schema.RelationshipTypes;
import model.api.base.Session;
import model.api.root.Root;
import model.api.token.Token;
import model.api.token.TokenPosition;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.TextualImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import util.NodeUtils;

/**
 * Created by vahidoo on 3/13/15.
 */
public class TokenImpl extends TextualImpl implements Token {
    public TokenImpl(Node node,Session session) {
        super(node,session);
    }

    @Override
    public Word getWord() {
        Node word = NodeUtils.singleNeighborhood(node, RelationshipTypes.CONTAINS_TOKEN, Direction.INCOMING, true);
//        return (Word) NodeContainerImpl.createNewInstance(WordImpl.class, word);
        return session.get(Word.class,word);
    }

    @Override
    public boolean hasRoot() {
        return getRoot() != null;
    }

//    public boolean hasLemma() {
//        return getLemma() != null;
//    }

    @Override
    public Root getRoot() {
        Node root = NodeUtils.singleNeighborhood(node, RelationshipTypes.HAS_ROOT, Direction.OUTGOING, false);
//        return (Root) NodeContainerImpl.createNewInstance(RootImpl.class, root);
        return session.get(Root.class,root);
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
        return (Integer) node.getProperty(NodeProperties.Token.index);
    }

    @Override
    public String getAddress() {
        return (String) node.getProperty(NodeProperties.Token.address);
    }

    @Override
    public Verse getVerse() {
        return getWord().getVerse();
    }

    @Override
    public int getChapterIndex() {
        String address = getAddress();
        return Integer.parseInt(address.split(":")[0]);
    }

//    @Override
//    public int getIndexInQuran() {
//        return (int) node.getProperty(NodeProperties.Token.indexInQuran);
//    }

    @Override
    public String toString() {
        return getAddress();
    }
}
