package model.impl.base;

import model.api.root.RootManager;
import model.impl.root.RootManagerImpl;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vahidoo on 3/13/15.
 */
public class ManagerFactory {
    private static Map<GraphDatabaseService,ManagerFactory> map = new HashMap<>();


    private RootManager rootIndex;

    public ManagerFactory(GraphDatabaseService database) {
        rootIndex = new RootManagerImpl(database);
    }

    public static ManagerFactory getFor(GraphDatabaseService database) {
        if (!map.containsKey(database)){
            map.put(database , new ManagerFactory(database));
        }
        return map.get(database);
    }

    public RootManager getRootIndex() {
        return rootIndex;
    }
}
