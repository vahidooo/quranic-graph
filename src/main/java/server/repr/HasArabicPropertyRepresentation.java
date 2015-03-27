package server.repr;

import data.schema.NodeProperties;
import org.neo4j.graphdb.Node;

/**
* Created by vahidoo on 3/9/15.
*/
public class HasArabicPropertyRepresentation extends Representation<Node>{
    @Override
    public String represent(Node node) {
        if ( !node.hasProperty(NodeProperties.GeneralText.arabic) )
            throw new RuntimeException( "Node #" + node.getId() +" don't has 'arabic' property." );

        return (String) node.getProperty(NodeProperties.GeneralText.arabic);

    }
}
