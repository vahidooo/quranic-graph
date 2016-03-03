package quran.app;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.xml.sax.SAXException;
import quran.app.base.BasicGraphConfiguration;
import quran.filler.LeedsDataFiller;
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
public class DataFillersLauncher /*extends BasicGraphConfiguration*/ {

    private static final String TANZIL_UTHMANI_PATH_KEY = "tanzil.uthmani.path";
    private static final String TANZIL_METADATA_PATH_KEY = "tanzil.metadata.path";


    private static final String LEEDS_CORPUS_PATH_KEY = "leeds.corpus.path";
    private static final String LEEDS_CORPUS_IGNORED_LINES_KEY = "leeds.corpus.ignoredLines";


    @Resource
    private Environment environment;

    @Bean
    public TanzilDataFiller tanzilDataFiller() {
        TanzilDataFiller tanzil = new TanzilDataFiller();
        tanzil.setMetadataFile(getClass().getClassLoader().getResourceAsStream(environment.getRequiredProperty(TANZIL_METADATA_PATH_KEY)));
        tanzil.setUthmaniFile(getClass().getClassLoader().getResourceAsStream(environment.getRequiredProperty(TANZIL_UTHMANI_PATH_KEY)));
        return tanzil;
    }

    @Bean
    public LeedsDataFiller leedsDataFiller() {
        LeedsDataFiller leeds = new LeedsDataFiller();
        leeds.setCorpusFile(getClass().getClassLoader().getResourceAsStream(environment.getRequiredProperty(LEEDS_CORPUS_PATH_KEY)));
        leeds.setIgnoredLines(Integer.parseInt(environment.getRequiredProperty(LEEDS_CORPUS_IGNORED_LINES_KEY)));
        return leeds;
    }


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataFillersLauncher.class);
        TanzilDataFiller tanzil = ctx.getBean(TanzilDataFiller.class);
        LeedsDataFiller leeds = ctx.getBean(LeedsDataFiller.class);

        tanzil.fill();
        leeds.fill();

        ctx.close();
    }

}
