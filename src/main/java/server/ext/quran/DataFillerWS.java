package server.ext.quran;

import data.filler.DataFiller;
import data.filler.DataFillerManager;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/19/15.
 */

@Path("/data")
public class DataFillerWS extends BaseWS {

    private static final Logger logger = Logger.getLogger(DataFiller.class.getName());
    private final Properties properties;

    public DataFillerWS(@Context GraphDatabaseService database, @Context ManagersSet managersSet) throws IOException {
        super(database, managersSet);

        InputStream stream = getClass().getClassLoader().getResourceAsStream(PluginWS.PLUGIN_CONF_PATH);
        this.properties = new Properties();
        properties.load(stream);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/fillers/list")
    public Response list() throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        Response response;
        try (Transaction tx = database.beginTx()) {

            List<DataFiller> dataFillers = new ArrayList<>();
            for (Class<? extends DataFiller> clazz : DataFillerManager.getTopologicalSorted()) {

                logger.info("try to construct " + clazz.getName());
                DataFiller df = null;
                Constructor constructor = clazz.getConstructor(GraphDatabaseService.class, ManagersSet.class, Properties.class);
                df = (DataFiller) constructor.newInstance(database, managersSet, properties);

                dataFillers.add(df);
            }

            String json = getJson("datafillers", dataFillers);
            response = getOkResponse(json);
            tx.success();
        }
        return response;

    }


    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/upgrade")
    public Response upgrade() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {


        Response response;
        List<DataFiller> dataFillers = new ArrayList<>();

        List<Class<? extends DataFiller>> sorted = DataFillerManager.getTopologicalSorted();

        for (Class<? extends DataFiller> clazz : sorted) {
            Constructor constructor = clazz.getConstructor(GraphDatabaseService.class, ManagersSet.class, Properties.class);
            DataFiller df = (DataFiller) constructor.newInstance(database, managersSet, properties);
            df.fill();
            dataFillers.add(df);
        }
//        String ret = getJson("classes", statuses);
        String ret = getJson("datafillers", dataFillers);
        response = Response.status(Response.Status.OK).entity((ret).getBytes(Charset.forName("UTF-8"))).build();
        return response;

    }
}