package server.ext.quran.init;

import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.PluginLifecycle;

import java.util.Collection;
import java.util.Collections;

public class DummyPluginInitializer implements PluginLifecycle {

    public DummyPluginInitializer() {
        System.out.println("DummyPluginInitializer -> constructor");
    }

    @Override
    public Collection<Injectable<?>> start(GraphDatabaseService graphDatabaseService, Configuration config) {
        System.out.println("DummyPluginInitializer -> start");

        return Collections.<Injectable<?>>singleton(new Injectable<Long>() {
            @Override
            public Long getValue() {
                return 42L;
            }

            @Override
            public Class<Long> getType() {
                return Long.class;
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("DummyPluginInitializer -> stop");

    }
}