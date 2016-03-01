package quran.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import quran.app.conf.BasicGraphConfiguration;

/**
 * Created by vahidoo on 2/25/16.
 */

@SpringBootApplication
@PropertySource("classpath:quran-app.properties")
@Import(BasicGraphConfiguration.class)
@ComponentScan(basePackages = "quran.service")
public class QuranApplication /*extends BasicGraphConfiguration */ {

    public static void main(String[] args) {
        SpringApplication.run(QuranApplication.class, args);
    }


}
