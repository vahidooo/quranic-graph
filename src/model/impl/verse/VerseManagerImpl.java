package model.impl.verse;

import base.GraphIndices;
import base.NodeProperties;
import model.api.verse.Verse;
import model.api.verse.VerseManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import util.NodeUtils;

/**
 * Created by vahidoo on 3/21/15.
 */
public class VerseManagerImpl implements VerseManager {
    private GraphDatabaseService database;

    public VerseManagerImpl(GraphDatabaseService database) {
        this.database = database;
    }

    @Override
    public Verse get(int chapter, int verse) {
        Node node = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.Verse.address, NodeUtils.getNodeAddress(chapter, verse)).getSingle();
        return new VerseImpl(node);
    }
}
