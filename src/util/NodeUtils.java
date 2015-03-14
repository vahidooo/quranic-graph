package util;

import base.NodeProperties;
import base.RelationshipTypes;
import org.jqurantree.arabic.ArabicText;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import java.util.Iterator;

/**
 * Created by vahidoo on 3/6/15.
 */
public class NodeUtils {


    public static void setPropertyAndIndex(Node node, String property, String indexName, Object value) {
        node.setProperty(property, value);
        Index<Node> index = node.getGraphDatabase().index().forNodes(indexName);
        index.add(node, property, value);
    }

    public static void setBuckwalterPropertyAndIndex(Node node, String indexName, String buckwalter) {

        ArabicText arabic = ArabicText.fromBuckwalter(buckwalter);
        ArabicText simpleArabic = arabic.removeDiacritics();
        String simpleBuckwalter = simpleArabic.toBuckwalter();

        NodeUtils.setPropertyAndIndex(node, NodeProperties.GeneralText.buckwalter, indexName, buckwalter);
        NodeUtils.setPropertyAndIndex(node, NodeProperties.GeneralText.simpleBuckwalter, indexName, simpleBuckwalter);
        NodeUtils.setPropertyAndIndex(node, NodeProperties.GeneralText.arabic, indexName, arabic.toUnicode());
        NodeUtils.setPropertyAndIndex(node, NodeProperties.GeneralText.simpleArabic, indexName, simpleArabic.toUnicode());

    }

    public static String getNodeAddress(Object prefix, int verse) {
        return String.format("%s:%d", prefix.toString(), verse);
    }

    public static Node singleNeighborhood(Node node, RelationshipTypes rel, Direction direction, boolean startNode) {
        Iterator<Relationship> it = node.getRelationships(rel, direction).iterator();
        if ( !it.hasNext() ){
            return null;
        }

        Relationship r = it.next();
        if (startNode)
            return r.getStartNode();
        return r.getEndNode();
    }

}
