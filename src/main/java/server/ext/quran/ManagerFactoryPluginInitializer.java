package server.ext.quran;

import model.impl.base.ManagersSet;
import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.PluginLifecycle;

import java.util.Collection;
import java.util.Collections;

public class ManagerFactoryPluginInitializer implements PluginLifecycle {

    @Override
    public Collection<Injectable<?>> start(final GraphDatabaseService database, Configuration config) {

        return Collections.<Injectable<?>>singleton(new Injectable<ManagersSet>() {
            @Override
            public ManagersSet getValue() {
                return new ManagersSet(database);
            }

            @Override
            public Class<ManagersSet> getType() {
                return ManagersSet.class;
            }
        });
    }

    @Override
    public void stop() {
    }
}