package model.impl.lemma;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.lemma.Lemma;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class LemmaFactory implements Factory<Lemma> {

    private Session session;

    public LemmaFactory(Session session) {
        this.session = session;
    }

    @Override
    public Lemma create(Node node) {
        return new LemmaImpl(node,session);
    }
}
