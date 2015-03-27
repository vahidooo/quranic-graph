package model.api.base;

import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public interface Factory<T> {
    T create(Node node);
}
