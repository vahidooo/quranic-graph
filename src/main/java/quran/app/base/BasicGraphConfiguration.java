package quran.app.base;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * Created by vahidoo on 3/1/16.
 */

@Configuration
@PropertySource("classpath:quran-app.properties")
@EnableNeo4jRepositories(basePackages = "quran.repository")
@EnableTransactionManagement(proxyTargetClass = true)
public class BasicGraphConfiguration extends Neo4jConfiguration {

    public static final String NEO4J_DB_PATH = "neo4j.db.path";

    @Resource
    private Environment environment;

    public BasicGraphConfiguration() {
        setBasePackage("quran.entity");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(environment.getRequiredProperty(NEO4J_DB_PATH))
//                .setConfig(ShellSettings.remote_shell_enabled, "true")
//                .setConfig(ShellSettings.remote_shell_read_only,"true")
//                .setConfig(ShellSettings.remote_shell_port, "8080")
//                .setConfig(ShellSettings.remote_shell_host, "0.0.0.0")
                .newGraphDatabase();


//        try {
//            GraphDatabaseAPI api = (GraphDatabaseAPI) db;
//
//            ServerConfigurator config = new ServerConfigurator(api);
//            config.configuration()
//                    .addProperty(Configurator.WEBSERVER_ADDRESS_PROPERTY_KEY, "127.0.0.1");
//            config.configuration()
//                    .addProperty(Configurator.WEBSERVER_PORT_PROPERTY_KEY, "7575");
//
//
//            WrappingNeoServerBootstrapper neoServerBootstrapper = new WrappingNeoServerBootstrapper(api, config);
//
//            neoServerBootstrapper.start();
//        } catch (Exception e) {
//            //handle appropriately
//        }


        return db;
    }

}
