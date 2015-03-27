package resource;

import org.junit.Test;
import server.ext.quran.ws.RootResource;
import ws.BaseWSTest;

/**
 * Created by vahidoo on 3/13/15.
 */
public class RootResourceTest extends BaseWSTest {





//    @Test
    public void sequenceTest(){

        String seq = "رحم-رحم" ;

        System.out.println( new RootResource(database, managersSet).sequence(seq) );

    }

    @Test
    public void distanceTest(){

        String roots = "دخل-شتت"  ;
        int window = 10;
        String mode = "word" ;

        System.out.println( new RootResource(database,managersSet).distance(mode, window, roots) );

    }

}
