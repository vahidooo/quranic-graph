package model.impl.base;

import model.api.base.NodeContainer;
import model.api.base.Session;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/11/15.
 */
public class NodeContainerImpl implements NodeContainer {

    protected Node node;
    protected Session session;

    public NodeContainerImpl(Node node, Session session) {
        this.node = node;
        this.session = session;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeContainer))
            return false;

        NodeContainer nc = (NodeContainer) obj;

        return node.equals(nc.getNode());
    }

//    public static NodeContainer createNewInstance(Class<? extends NodeContainerImpl> clazz, Node n) {
//        if (n == null)
//            return null;
//
//        try {
//            Constructor<? extends NodeContainerImpl> constructor = clazz.getDeclaredConstructor(Node.class);
//            return constructor.newInstance(n);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
