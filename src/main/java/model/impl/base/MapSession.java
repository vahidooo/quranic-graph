package model.impl.base;

import model.api.base.Factory;
import model.api.base.Session;
import org.neo4j.graphdb.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/27/15.
 */
public class MapSession implements Session {

    private static final Logger logger = Logger.getLogger(MapSession.class.getName());

    private Map<Class<?>, Factory> factories;
    private Map<Class<?>, Map<Node, Object>> map;

    public MapSession() {
        map = new HashMap<>();
    }

    public synchronized void setFactories(Map<Class<?>, Factory> factories) {
        this.factories = factories;

        for (Map.Entry<Class<?>, Factory> entry : factories.entrySet()) {
            logger.info(entry.toString());
        }
    }

    @Override
    public <T> T get(Class<T> clazz, Node node) {

        synchronized (this) {
            if (factories == null) {
                throw new RuntimeException();
            }
        }

        if (node == null || clazz == null)
            return null;

        if (!map.containsKey(clazz)) {
            map.put(clazz, new HashMap<Node, Object>());
        }

        Map<Node, Object> m = map.get(clazz);
        if (m.containsKey(node))
            return (T) m.get(node);


        Factory factory = factories.get(clazz);
        Object object = factory.create(node);
        m.put(node, object);

        return (T) object;
    }

    @Override
    public <T> Iterable<T> get(Class<T> clazz, Iterable<Node> nodes) {
        List<T> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(get(clazz, node));
        }
        return list;
    }

}
