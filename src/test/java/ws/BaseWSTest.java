package ws;

import model.impl.base.ManagersSet;
import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Created by vahidoo on 3/27/15.
 */
public class BaseWSTest {

    protected static final String DB_PATH = "/home/vahidoo/projects/graph/quranic-graph/neo4j/data/graph.db";
    protected GraphDatabaseService database;
    protected ManagersSet managersSet;


    @Before
    public void setUp() throws Exception {
        database = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        managersSet = new ManagersSet(database);

    }

    @After
    public void tearDown() throws Exception {
        database.shutdown();
    }

}
