package model.impl.lemma;

import data.schema.RelationshipTypes;
import model.api.base.Session;
import model.api.lemma.Lemma;
import model.api.root.Root;
import model.api.token.Token;
import model.impl.base.TextualImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import util.NodeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by vahidoo on 3/11/15.
 */
public class LemmaImpl extends TextualImpl implements Lemma {

    LemmaImpl(Node node, Session session) {
        super(node, session);
    }

    @Override
    public Set<Token> getTokens() {
        Set<Token> tokens = new HashSet<>();

        Iterator<Relationship> it = node.getRelationships(Direction.INCOMING, RelationshipTypes.HAS_LEMMA).iterator();
        while (it.hasNext()) {
            Node tokenNode = it.next().getStartNode();
            Token token = session.get(Token.class, tokenNode);
            tokens.add(token);
        }
        return tokens;
    }

    @Override
    public boolean hasRoot() {
        return getRoot() != null;
    }

    @Override
    public Root getRoot() {
        Node root = NodeUtils.singleNeighborhood(node, RelationshipTypes.LEMMA_HAS_ROOT, Direction.OUTGOING, false);
        return session.get(Root.class, root);
    }

    @Override
    public String toString() {
        return getBuckwalter();
    }

    @Override
    public int hashCode() {
        return getBuckwalter().hashCode();
    }
}
