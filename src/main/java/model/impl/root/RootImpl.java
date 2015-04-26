package model.impl.root;

import data.schema.RelationshipTypes;
import model.api.base.Session;
import model.api.lemma.Lemma;
import model.api.root.Root;
import model.api.token.Token;
import model.impl.base.TextualImpl;
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

    RootImpl(Node node, Session session) {
        super(node, session);
    }

    @Override
    public Set<Token> getTokens() {
        Set<Token> tokens = new HashSet<>();

        Iterator<Relationship> it = node.getRelationships(Direction.INCOMING, RelationshipTypes.HAS_ROOT).iterator();
        while (it.hasNext()) {
            Node tokenNode = it.next().getStartNode();
            Token token = session.get(Token.class , tokenNode);
            tokens.add(token);
        }
        return tokens;
    }

    @Override
    public Set<Lemma> getLemmas() {
        Set<Lemma> lemmas = new HashSet<>();

        Iterator<Relationship> it = node.getRelationships(Direction.INCOMING, RelationshipTypes.LEMMA_HAS_ROOT).iterator();
        while (it.hasNext()) {
            Node lemmaNode = it.next().getStartNode();
            Lemma lemma = session.get(Lemma.class , lemmaNode);
            lemmas.add(lemma);
        }
        return lemmas;
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
