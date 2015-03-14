package model.impl.root;

import base.GraphIndices;
import base.NodeProperties;
import model.api.root.Root;
import model.api.root.RootManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/11/15.
 */
public class RootManagerImpl implements RootManager {

    private GraphDatabaseService database;

    public RootManagerImpl(GraphDatabaseService database) {
        this.database = database;
    }

    public Root getRootByArabic(String root) {
        Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();

        if (rootNode == null)
            return null;

        return new RootImpl(rootNode);
    }
}
