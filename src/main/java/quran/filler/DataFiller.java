package quran.filler;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 2/25/16.
 */
public interface DataFiller {
    void fill() throws ParserConfigurationException, IOException, SAXException;
}
