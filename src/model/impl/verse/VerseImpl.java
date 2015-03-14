package model.impl.verse;

import base.NodeProperties;
import model.api.verse.Verse;
import model.impl.base.NodeContainerImpl;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/13/15.
 */
public class VerseImpl extends NodeContainerImpl implements Verse {
    public VerseImpl(Node node) {
        super(node);
    }

    @Override
    public String getAddress() {
        return (String) node.getProperty(NodeProperties.General.address);
    }
}
