package data.filler;

import data.schema.GraphIndices;
import data.schema.NodeProperties;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/19/15.
 */
public abstract class DataFiller {

    public static final String INITIAL_PROGRESS = "-";

    protected abstract List<TransactionalFiller> getTransactionalFillers() throws Throwable;

    protected GraphDatabaseService database;
    protected ManagersSet managersSet;
    protected Properties properties;

    protected Node node;

    protected static final Logger logger = Logger.getLogger(DataFiller.class.getName());


    enum State {
        FILLED, NOT_FILLED, FILLING_ERROR, DEPENDENCY_ERROR, PENDING
    }

    public DataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        this.database = database;
        this.properties = properties;
        this.managersSet = managersSet;
        initNode();
    }

    private void initNode() {
        new DataFillerManager(database);
        try (Transaction tx = database.beginTx()) {
            node = database.index().forNodes(GraphIndices.DataFillerIdx).get(NodeProperties.DataFiller.clazz, this.getClass().getName()).getSingle();
            tx.success();
        }
    }

    public void fill() {

        try {
            if (!(getStatus().equals(State.FILLED) || ((getStatus().equals(State.PENDING)) && !getProgress().equals(INITIAL_PROGRESS)))) {
                logger.info("Filling starts:" + getClass().getName());
                setState(State.PENDING);

                List<TransactionalFiller> txFillers = getTransactionalFillers();
                for (int i = 0; i < txFillers.size(); i++) {
                    TransactionalFiller filler = txFillers.get(i);
                    try (Transaction tx = database.beginTx()) {
                        logger.info("filler : " + filler);
                        filler.fillInTransaction(database);
                        node.setProperty(NodeProperties.DataFiller.progress, (i + 1) + "/" + txFillers.size());
                        tx.success();
                    } catch (Throwable th) {
                        logger.info("Filling Failed:" + getClass().getName());
                        setState(State.FILLING_ERROR);
                        throw new RuntimeException("internal error", th);
                    }
                }
                setState(State.FILLED);
                logger.info("Filling finished Successfully:" + getClass().getName());
            }
        } catch (Throwable th) {
            throw new RuntimeException(th);
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

    public String getProgress() {
        String progress;
        try (Transaction tx = database.beginTx()) {
            progress = (String) node.getProperty(NodeProperties.DataFiller.progress);
            tx.success();
        }
        return progress;
    }

}
