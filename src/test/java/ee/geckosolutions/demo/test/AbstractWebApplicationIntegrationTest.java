package ee.geckosolutions.demo.test;

import ee.geckosolutions.demo.TestApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
public abstract class AbstractWebApplicationIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
