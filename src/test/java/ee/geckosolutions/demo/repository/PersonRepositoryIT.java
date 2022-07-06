package ee.geckosolutions.demo.repository;

import ee.geckosolutions.demo.config.Constants;
import ee.geckosolutions.demo.domain.Person;
import ee.geckosolutions.demo.test.AbstractDataJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PersonRepositoryIT extends AbstractDataJpaIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Transactional
    void testThatSaveAllIsSuccessful() {
        // given
        Person person = Person.builder().name("Test Name").imageUrl("https://localhost:8888/imageUrl").build();

        // when
        List<Person> personList = personRepository.saveAll(Collections.singleton(person));

        // then
        Person savedPerson = personList.get(0);
        assertThat(savedPerson.getId(), is(equalTo(1L)));
        assertThat(savedPerson.getCreatedBy(), is(equalTo(Constants.SYSTEM_ACCOUNT)));
    }

}
