package base;

import org.neo4j.graphdb.GraphDatabaseService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 3/5/15.
 */
public interface Importer {
    void doImport(GraphDatabaseService graphDB ) throws ParserConfigurationException, IOException, SAXException;
}
