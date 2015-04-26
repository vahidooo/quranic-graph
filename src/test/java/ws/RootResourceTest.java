package ws;

import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import server.ext.quran.ws.RootWS;

/**
 * Created by vahidoo on 3/13/15.
 */
public class RootResourceTest extends BaseWSTest {





//    @Test
    public void sequenceTest(){

        String seq = "رحم-رحم" ;

        System.out.println( new RootWS(database, managersSet).sequence(seq) );

    }

//    @Test
    public void distanceTest(){

        String roots = "دخل-شتت"  ;
        int window = 10;
        String mode = "word" ;

        System.out.println( new RootWS(database,managersSet).distance(mode, window, roots) );

    }

    @Test
    public void lemmas(){
        String root = "رحم" ;

        try(Transaction tx = database.beginTx()) {
            System.out.println(new RootWS(database, managersSet).lemmas(root));
            tx.success();
        }

    }

}
