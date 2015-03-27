package model.api.base;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface NodeContainer {

    @JsonIgnore
    Node getNode();

    @JsonIgnore
    Session getSession();


}
