package resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import server.ext.quran.ws.RootResource;

/**
 * Created by vahidoo on 3/13/15.
 */
public class RootResourceTest {

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

//    @Test
    public void sequenceTest(){

        String seq = "رحم-رحم" ;

        System.out.println( new RootResource(database).sequence(seq) );

    }

    @Test
    public void distanceTest(){

        String roots = "دخل-شتت"  ;
        int window = 10;
        String mode = "word" ;

        System.out.println( new RootResource(database).distance(mode, window, roots) );

    }

}
