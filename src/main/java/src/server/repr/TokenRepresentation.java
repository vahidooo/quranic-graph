package server.repr;


import base.NodeLabels;
import base.NodeProperties;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/9/15.
 */
public class TokenRepresentation extends Representation<Node> {


    @Override
    public String represent(Node node) {
        if ( !node.hasLabel(NodeLabels.TOKEN) )
            throw new RuntimeException( "Node #" + node.getId() +" don't has Token label." );

        StringBuilder sb = new StringBuilder();
//        sb.append(node.getProperty(NodeProperties.General.address));
//        sb.append( " >> " );
        sb.append(node.getProperty(NodeProperties.GeneralText.arabic));
        return sb.toString();

    }
}
