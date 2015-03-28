package server.ext.quran.ws;

import model.impl.base.ManagersSet;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/19/15.
 */

@Path("/info")
public class PluginWS extends BaseWS {

    public static final String PLUGIN_CONF_PATH = "quranic-graph-plugin.properties";
    private static final Logger logger = Logger.getLogger(PluginWS.class.getName());
    private final Properties properties;

    public PluginWS(@Context GraphDatabaseService database, @Context ManagersSet managersSet) throws IOException {
        super(database, managersSet);

        this.properties = new Properties();

        logger.info(PLUGIN_CONF_PATH);
        properties.load(getClass().getClassLoader().getResourceAsStream(PLUGIN_CONF_PATH));
        logger.info("properties loaded");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/properties")
    public Response status() throws IOException, ClassNotFoundException {
        logger.info("/info/properties ");
        String json = getJson("properties", properties);
        return Response.status(Response.Status.OK).entity((json).getBytes(Charset.forName("UTF-8"))).build();
    }

}