package model.api.base;

import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/26/15.
 */
public interface Session {

    <T> T get( Class<T> clazz, Node node );
    <T> Iterable<T> get( Class<T> clazz, Iterable<Node> node );

//    <T extends NodeContainer> void evict( T object);

//    boolean contains(Node node);

}
