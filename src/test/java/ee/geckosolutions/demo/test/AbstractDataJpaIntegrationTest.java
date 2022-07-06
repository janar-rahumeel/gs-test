package ee.geckosolutions.demo.test;

import ee.geckosolutions.demo.config.DatabaseConfiguration;
import ee.geckosolutions.demo.security.SpringSecurityAuditorAware;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import({AbstractDataJpaIntegrationTest.DataJpaTestConfiguration.class, DatabaseConfiguration.class})
@DataJpaTest
@ActiveProfiles("it")
public abstract class AbstractDataJpaIntegrationTest {

    @TestConfiguration
    static class DataJpaTestConfiguration {

        @Bean
        public SpringSecurityAuditorAware springSecurityAuditorAware() {
            return new SpringSecurityAuditorAware();
        }

    }


}
