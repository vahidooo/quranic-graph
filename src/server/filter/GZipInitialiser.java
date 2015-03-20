package server.filter;

/**
 * Created by vahidoo on 3/20/15.
 */

import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.AbstractNeoServer;
import org.neo4j.server.NeoServer;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.SPIPluginLifecycle;
import org.neo4j.server.web.WebServer;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

public class GZipInitialiser implements SPIPluginLifecycle {

    private static final Logger logger = Logger.getLogger(GZipInitialiser.class.getName());

    static {
        logger.info("SALAM");
    }

    private WebServer webServer;

    @Override
    public Collection<Injectable<?>> start(NeoServer neoServer) {
        webServer = getWebServer(neoServer);
        RequestDumperFilter filter = new RequestDumperFilter();

        webServer.addFilter(filter, "/data/*");
        return Collections.emptyList();

    }

    private WebServer getWebServer(final NeoServer neoServer) {
        if (neoServer instanceof AbstractNeoServer) {
            return ((AbstractNeoServer) neoServer).getWebServer();
        }
        throw new IllegalArgumentException("expected AbstractNeoServer");
    }

    @Override
    public Collection<Injectable<?>> start(GraphDatabaseService graphDatabaseService, Configuration configuration) {
        throw new IllegalAccessError();
    }

    @Override
    public void stop() {

    }
}

