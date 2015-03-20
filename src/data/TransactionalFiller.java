package data;

import org.neo4j.graphdb.GraphDatabaseService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 3/20/15.
 */
public interface TransactionalFiller {

    void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException;

}
