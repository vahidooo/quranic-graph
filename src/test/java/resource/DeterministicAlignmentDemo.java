package resource;


import model.impl.base.ManagersSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import server.ext.quran.ws.RootWS;
 
/*
 * Created on Mar 28, 2006
 */

/**
 * This is a demo class that performes both a local and a global alignment
 * from two given sequences. The result is printed on the screen.
 * Therfore a substitution matrix file is required, which can be downloaded
 * at {@link ftp://ftp.ncbi.nlm.nih.gov/blast/matrices/}.
 * This demo only works for DNA sequences. However, the alignment algorithms
 * are able to use any kind of alphabet as long as there is a substitution
 * matrix available. For this example the matrix NUC.4.4 is the best one.
 *
 * @author Andreas Dr&auml;ger
 */
public class DeterministicAlignmentDemo {

    /**
     * This performs an alignment of two given sequences and
     * prints the result on the screen.
     *
     * @param args: a query and a target sequence
     * and one file containing the substitution matrix to be used.
     * {@link ftp://ftp.ncbi.nlm.nih.gov/blast/matrices/}
     */
//    public static void main(String args[]) {

//        SmithWaterman sw = new SmithWaterman();
//
//        List<Root> first = null, second = null;
//
//        RootCompoundSet cs = new RootCompoundSet(first, second);
//
//        sw.setQuery(new ArrayListSequenceReader(cs.getCompounds(0), cs));
//        sw.setTarget(new ArrayListSequenceReader(cs.getCompounds(1), cs));
//
//        GapPenalty penalty = new SimpleGapPenalty(1, 0);
//        sw.setGapPenalty(penalty);
//
//        RootSubstitutionMatrix matrix = new RootSubstitutionMatrix(cs,(short) 2,(short) -3);
//        sw.setSubstitutionMatrix(matrix);
//
//        SequencePair pair = sw.getPair();
//        System.out.println(sw.getMaxScore());
//    }


    private static final String DB_PATH = "/home/vahidoo/projects/graph/quranic-graph/neo4j/data/graph.db";
    private GraphDatabaseService database;
    private ManagersSet managersSet;

    @Before
    public void setUp() throws Exception {
        database = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        managersSet = new ManagersSet(database);
    }

    @After
    public void tearDown() throws Exception {
        database.shutdown();
    }

    @Test
    public void smithWatermanTest() {

        RootWS ws = new RootWS(database,managersSet);
        ws.smithWaterman(27, 87, 3);
//        ws.smithWaterman(114, 1 , 3);


    }
}