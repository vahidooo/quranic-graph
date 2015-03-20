package data;

import base.GraphIndices;
import base.NodeProperties;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import util.NodeUtils;
import utils.ReflectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/19/15.
 */
public abstract class DataFiller {

    protected abstract List<TransactionalFiller> getTransactionalFillers() throws Throwable;

    protected GraphDatabaseService database;
    protected Properties properties;
    protected Node node;

    protected static final Logger logger = Logger.getLogger(DataFiller.class.getName());


    enum State {
        FILLED, NOT_FILLED, FILLING_ERROR, DEPENDENCY_ERROR, PENDING
    }

    public DataFiller(GraphDatabaseService database, Properties properties) {
        this.database = database;
        this.properties = properties;
        initNode();
    }

    private void initNode() {

        try (Transaction tx = database.beginTx()) {

            node = database.index().forNodes(GraphIndices.DataFillerIdx).get(NodeProperties.DataFiller.clazz, this.getClass().getName()).getSingle();
            if (node == null) {
                node = database.createNode();
                NodeUtils.setPropertyAndIndex(node, NodeProperties.DataFiller.clazz, GraphIndices.DataFillerIdx, this.getClass().getName());
                node.setProperty(NodeProperties.DataFiller.state, State.NOT_FILLED.name());
                node.setProperty(NodeProperties.DataFiller.progress, 0);
            }

            for (Class<? extends DataFiller> clazz : DataFillerManager.getDependencies(this.getClass())) {
                DataFiller dependency = ReflectionUtils.createNewDataFiller(clazz, database);
                if (dependency.getStatus() != State.FILLED) {
                    node.setProperty(NodeProperties.DataFiller.state, State.DEPENDENCY_ERROR.name());
                }
            }
            tx.success();
        }
    }

    public void fill() {

        try {
            if (!(getStatus().equals(State.FILLED) || (getStatus().equals(State.PENDING)))) {
                logger.info("Filling starts:" + getClass().getName());
                setState(State.PENDING);

                for (int i = 0; i < getTransactionalFillers().size(); i++) {
                    TransactionalFiller filler = getTransactionalFillers().get(i);
                    try (Transaction tx = database.beginTx()) {
                        logger.info("filler : " + filler);
                        filler.fillInTransaction(database);
                        node.setProperty(NodeProperties.DataFiller.progress, i);
                        tx.success();
                    }
                }
                setState(State.FILLED);
                logger.info("Filling finished Successfully:" + getClass().getName());
            }
        } catch (Throwable th) {
            logger.info("Filling Failed:" + getClass().getName());
            setState(State.FILLING_ERROR);
            throw new RuntimeException("internal error", th);
        }

    }

    private void setState(State state) {
        try (Transaction tx = database.beginTx()) {
            node.setProperty(NodeProperties.DataFiller.state, state.name());
            tx.success();
        }
    }

//    protected static List<Class<? extends DataFiller>> getDependenciesList(Class<? extends DataFiller>... classes) {
//        List<Class<? extends DataFiller>> list = new ArrayList<>();
//        for (Class<? extends DataFiller> clazz : classes) {
//            list.add(clazz);
//        }
//        return list;
//    }

    public State getStatus() {
        String state;
        try (Transaction tx = database.beginTx()) {
            state = (String) node.getProperty(NodeProperties.DataFiller.state);
            tx.success();
        }
        return State.valueOf(state);
    }

    public String getName() {
        return getClass().getName();
    }

}
