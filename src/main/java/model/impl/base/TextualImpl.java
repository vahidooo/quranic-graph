package model.impl.base;

import data.schema.NodeProperties;
import model.api.base.Session;
import model.api.base.Textual;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/13/15.
 */
public class TextualImpl extends NodeContainerImpl implements Textual {

    public TextualImpl(Node node, Session session) {
        super(node, session);
    }

    @Override
    public String getArabic() {
        return (String) node.getProperty(NodeProperties.GeneralText.arabic);
    }

    @Override
    public String getBuckwalter() {
        return (String) node.getProperty(NodeProperties.GeneralText.buckwalter);
    }

    @Override
    public String getSimpleArabic() {
        return (String) node.getProperty(NodeProperties.GeneralText.simpleArabic);
    }

    @Override
    public String getSimpleBuckwalter() {
        return (String) node.getProperty(NodeProperties.GeneralText.simpleBuckwalter);
    }
}
