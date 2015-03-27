package model.impl.root;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.root.Root;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class RootFactory implements Factory<Root> {

    private Session session;

    public RootFactory(Session session) {
        this.session = session;
    }

    @Override
    public Root create(Node node) {
        return new RootImpl(node,session);
    }
}
