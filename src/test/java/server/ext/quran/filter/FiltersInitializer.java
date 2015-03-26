package server.ext.quran.filter;

import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.AbstractNeoServer;
import org.neo4j.server.NeoServer;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.configuration.ThirdPartyJaxRsPackage;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.SPIPluginLifecycle;
import org.neo4j.server.web.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FiltersInitializer implements SPIPluginLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(FiltersInitializer.class);
    private WebServer webServer;

    private ConvertKafYaInParametersFilter convertKafYaInParametersFilter;

    private static Map<Class<?>, List<String>> mappings;

    static {
        mappings = new HashMap<>();

        mappings.put(ConvertKafYaInParametersFilter.class, new ArrayList<String>());
        mappings.get(ConvertKafYaInParametersFilter.class).add("/*");

    }


    @Override
    public Collection<Injectable<?>> start(final GraphDatabaseService graphDatabaseService, final Configuration config) {
        throw new IllegalAccessError();
    }

    @Override
    public void stop() {
        if (convertKafYaInParametersFilter != null) {
            for (String path : mappings.get(ConvertKafYaInParametersFilter.class)) {
                webServer.removeFilter(convertKafYaInParametersFilter, path);
            }
        }
    }

    @Override
    public Collection<Injectable<?>> start(final NeoServer neoServer) {
        logger.info("start");

        convertKafYaInParametersFilter = new ConvertKafYaInParametersFilter();
        for (String path : mappings.get(ConvertKafYaInParametersFilter.class)) {
            webServer.addFilter(convertKafYaInParametersFilter, path);
        }

        return Collections.emptyList();
    }

    private WebServer getWebServer(final NeoServer neoServer) {
        if (neoServer instanceof AbstractNeoServer) {
            return ((AbstractNeoServer) neoServer).getWebServer();
        }
        throw new IllegalArgumentException("expected AbstractNeoServer");
    }

    private String getMyMountpoint(final Configurator configurator) {
        final String packageName = getClass().getPackage().getName();

        for (ThirdPartyJaxRsPackage o : configurator.getThirdpartyJaxRsPackages()) {
            if (o.getPackageName().equals(packageName)) {
                return o.getMountPoint();
            }
        }
        throw new RuntimeException("unable to resolve our mountpoint?");
    }
}