package model.impl.verse;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.verse.Verse;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class VerseFactory implements Factory<Verse> {

    private Session session;

    public VerseFactory(Session session) {
        this.session = session;
    }

    @Override
    public Verse create(Node node) {
        return new VerseImpl(node,session);
    }
}
