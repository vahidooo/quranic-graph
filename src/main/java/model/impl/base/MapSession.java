package model.impl.base;

import model.api.base.Factory;
import model.api.base.Session;
import org.neo4j.graphdb.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vahidoo on 3/27/15.
 */
public class MapSession implements Session {

    private Map<Class<?>, Factory> factories;
    private Map<Class<?>, Map<Node, Object>> map;

    public MapSession() {
        map = new HashMap<>();
    }

    public void setFactories(Map<Class<?>, Factory> factories) {
        this.factories = factories;
    }

    @Override
    public <T> T get(Class<T> clazz, Node node) {

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
