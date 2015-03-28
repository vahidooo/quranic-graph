import data.filler.DataFillerManager;
import data.filler.leeds.more.RootOfLemmaDataFiller;
import org.junit.Test;
import ws.BaseWSTest;

/**
 * Created by vahidoo on 3/29/15.
 */
public class DataFillerTest extends BaseWSTest {

    @Test
    public void rootOfLemmaTest() {

        new DataFillerManager(database);
        RootOfLemmaDataFiller filler = new RootOfLemmaDataFiller(database, managersSet, null);
        filler.fill();

    }


}
