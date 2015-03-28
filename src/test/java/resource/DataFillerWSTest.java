package resource;

import org.junit.Test;
import server.ext.quran.ws.DataFillerWS;
import ws.BaseWSTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by vahidoo on 3/13/15.
 */
public class DataFillerWSTest extends BaseWSTest {

    @Test
    public void upgradeTest() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFillerWS ws = new DataFillerWS(database,managersSet);
        ws.upgrade();

    }

//    @Test
    public void status() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DataFillerWS ws = new DataFillerWS(database,managersSet);
        ws.list();

    }

}
