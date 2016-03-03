package quran.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import quran.app.base.BasicGraphConfiguration;

/**
 * Created by vahidoo on 2/25/16.
 */

@SpringBootApplication
@PropertySource("classpath:quran-app.properties")
@Import(BasicGraphConfiguration.class)
@ComponentScan(basePackages = "quran.service")
public class QuranApplication extends SpringBootServletInitializer /*implements CommandLineRunner *//*extends BasicGraphConfiguration */ {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(QuranApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(QuranApplication.class, args);
    }



//    @Autowired
//    private GraphDatabaseService graphDatabaseService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            WrappingNeoServerBootstrapper neoServerBootstrapper;
//            GraphDatabaseAPI api = (GraphDatabaseAPI) graphDatabaseService;
//
//            ServerConfigurator config = new ServerConfigurator(api);
//            config.configuration()
//                    .addProperty(Configurator.WEBSERVER_ADDRESS_PROPERTY_KEY, "127.0.0.1");
//            config.configuration()
//                    .addProperty(Configurator.WEBSERVER_PORT_PROPERTY_KEY, "7474");
//
//            neoServerBootstrapper = new WrappingNeoServerBootstrapper(api, config);
//            neoServerBootstrapper.start();
//        } catch (Exception e) {
//            //handle appropriately
//        }
//    }
}
