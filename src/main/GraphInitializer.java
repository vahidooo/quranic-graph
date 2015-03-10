package main;

import graph.quran.corpus.leeds.LeedsCorpusImporter;
import graph.quran.tanzil.TanzilImporter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 3/5/15.
 */
public class GraphInitializer {

    public static final String DB_PATH = "/home/vahidoo/Desktop/neo4j-community-2.1.5/data/graph.db";
    private static final String TANZIL_METADATA_PATH = "raw-data/quran-data.xml";
    private static final String TANZIL_UTHMANI_PATH = "raw-data/quran-uthmani.xml";
    private static final String LEEDS_CORPUS_PATH = "raw-data/quranic-corpus-morphology-0.4.txt";


    private GraphDatabaseService graphDB;

    public GraphInitializer() {
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }

    public void close() {
        graphDB.shutdown();
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        GraphInitializer initializer = new GraphInitializer();
        TanzilImporter tanzilImporter = new TanzilImporter(TANZIL_UTHMANI_PATH,TANZIL_METADATA_PATH);
        LeedsCorpusImporter leedsCorpusImporter = new LeedsCorpusImporter(LEEDS_CORPUS_PATH);

        tanzilImporter.doImport(initializer.graphDB);
        leedsCorpusImporter.doImport(initializer.graphDB , 10000 );

        initializer.close();
    }



}
