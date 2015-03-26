package model.impl.base;

import model.api.root.RootManager;
import model.api.verse.VerseManager;
import model.impl.root.RootManagerImpl;
import model.impl.verse.VerseManagerImpl;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vahidoo on 3/13/15.
 */
public class ManagerFactory {
    private static Map<GraphDatabaseService,ManagerFactory> map = new HashMap<>();


    private RootManager rootManager;
    private VerseManager verseManager;

    public ManagerFactory(GraphDatabaseService database) {
        rootManager = new RootManagerImpl(database);
        verseManager = new VerseManagerImpl(database);
    }

    public static ManagerFactory getFor(GraphDatabaseService database) {
        if (!map.containsKey(database)){
            map.put(database , new ManagerFactory(database));
        }
        return map.get(database);
    }

    public RootManager getRootManager() {
        return rootManager;
    }

    public VerseManager getVerseManager() {
        return verseManager;
    }
}
