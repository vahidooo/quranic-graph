package model.impl.root;

import base.GraphIndices;
import base.NodeLabels;
import base.NodeProperties;
import model.api.root.Root;
import model.api.root.RootManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.tooling.GlobalGraphOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vahidoo on 3/11/15.
 */
public class RootManagerImpl implements RootManager {

    private GraphDatabaseService database;

    public RootManagerImpl(GraphDatabaseService database) {
        this.database = database;
    }


    public List<Root> getAll() {
        List<Root> roots = new ArrayList<>();
        ResourceIterable<Node> nodes = GlobalGraphOperations.at(database).getAllNodesWithLabel(NodeLabels.ROOT);

        for (Node node : nodes) {
            Root root = new RootImpl(node);
            roots.add(root);
        }
        return roots;
    }

    public Root getRootByArabic(String root) {
        Node rootNode = database.index().forNodes(GraphIndices.RootIndex).get(NodeProperties.GeneralText.arabic, root).getSingle();

        if (rootNode == null)
            return null;

        return new RootImpl(rootNode);
    }


}
