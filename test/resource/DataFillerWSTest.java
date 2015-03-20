package resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import server.ext.quran.DataFillerWS;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by vahidoo on 3/13/15.
 */
public class DataFillerWSTest {

    private static final String DB_PATH = "/home/vahidoo/projects/graph/quranic-graph/neo4j/data/graph.db";
    private GraphDatabaseService database;

    @Before
    public void setUp() throws Exception {
        database = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }

    @After
    public void tearDown() throws Exception {
        database.shutdown();
    }


    @Test
    public void upgradeTest() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFillerWS ws = new DataFillerWS(database);
        ws.upgrade();

    }

//    @Test
    public void status() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFillerWS ws = new DataFillerWS(database);
        ws.list();

    }

}
