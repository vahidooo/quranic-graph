package quran.app.conf;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.xml.sax.SAXException;
import quran.filler.TanzilDataFiller;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by vahidoo on 2/25/16.
 */

@PropertySource({"classpath:fillers.properties", "classpath:quran-app.properties"})
@Configuration
@Import(BasicGraphConfiguration.class)
public class FillersConfiguration /*extends BasicGraphConfiguration*/ {

    private static final String TANZIL_UTHMANI_PATH_KEY = "tanzil.uthmani.path";
    private static final String TANZIL_METADATA_PATH_KEY = "tanzil.metadata.path";

    @Resource
    private Environment environment;

    @Bean
    public TanzilDataFiller tanzilDataFiller() {
        TanzilDataFiller tanzil = new TanzilDataFiller();
        tanzil.setMetadataFile(getClass().getClassLoader().getResourceAsStream(environment.getRequiredProperty(TANZIL_METADATA_PATH_KEY)));
        tanzil.setUthmaniFile(getClass().getClassLoader().getResourceAsStream(environment.getRequiredProperty(TANZIL_UTHMANI_PATH_KEY)));
        return tanzil;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(FillersConfiguration.class);
        TanzilDataFiller tanzil = ctx.getBean(TanzilDataFiller.class);

        tanzil.fill();
        ctx.close();
    }

}
