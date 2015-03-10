package util;

import base.NodeProperties;
import org.jqurantree.arabic.ArabicText;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

/**
 * Created by vahidoo on 3/6/15.
 */
public class NodeUtils {


    public static void setPropertyAndIndex(Node node, String property, String indexName, Object value) {
        node.setProperty(property,value);
        Index<Node> index = node.getGraphDatabase().index().forNodes(indexName);
        index.add(node,property,value);
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

    public static String getVerseAddress(int chapter , int verse){
        return String.format("%d:%d" , chapter , verse);
    }

}
