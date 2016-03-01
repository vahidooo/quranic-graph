package quran.app.conf;

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
//        setGraphDatabaseService(new GraphDatabaseFactory().newEmbeddedDatabase(environment.getRequiredProperty(NEO4J_DB_PATH)));
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(environment.getRequiredProperty(NEO4J_DB_PATH));
    }

}
