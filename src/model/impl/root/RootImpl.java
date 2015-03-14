package model.impl.root;

import base.RelationshipTypes;
import model.api.root.Root;
import model.api.token.Token;
import model.impl.base.TextualImpl;
import model.impl.token.TokenImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by vahidoo on 3/11/15.
 */
public class RootImpl extends TextualImpl implements Root {

    public RootImpl(Node node) {
        super(node);
    }

    @Override
    public Set<Token> getTokens() {
        Set<Token> tokens = new HashSet<>();

        Iterator<Relationship> it = node.getRelationships(Direction.INCOMING, RelationshipTypes.HAS_ROOT).iterator();
        while (it.hasNext()) {
            Node tokenNode = it.next().getStartNode();
            Token token = new TokenImpl(tokenNode);
            tokens.add(token);
        }
        return tokens;
    }
}
