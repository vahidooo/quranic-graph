package resource;

import base.GraphIndices;
import base.NodeProperties;
import graph.quran.corpus.leeds.LeedsCorpusImporter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by vahidoo on 3/13/15.
 */
public class LeedsImporterTest {

    private static final String DB_PATH = "/home/vahidoo/projects/graph/quranic-graph/neo4j/data/graph.db";
    private static final String LEEDS_CORPUS_PATH = "/home/vahidoo/projects/graph/quranic-graph/resources/quranic-corpus-morphology-0.4.txt";
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
    public void importTest() throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(LEEDS_CORPUS_PATH));

        for (int i = 0; i < LeedsCorpusImporter.IGNORED_LINES; i++) {
            scanner.nextLine();
        }


        try (Transaction tx = database.beginTx()) {
            int count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] parts = line.split("\t");
                String address = parts[0].replaceAll("\\(", "").replaceAll("\\)", "");

                Node n = database.index().forNodes(GraphIndices.TokenIndex).get(NodeProperties.General.address, address).getSingle();
                Assert.assertNotNull(n);

                count++;
                System.out.println(count);
            }
            tx.success();
        }


    }

}
