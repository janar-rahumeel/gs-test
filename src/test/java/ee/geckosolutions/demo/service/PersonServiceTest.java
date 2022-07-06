package ee.geckosolutions.demo.service;

import ee.geckosolutions.demo.domain.Person;
import ee.geckosolutions.demo.repository.PersonRepository;
import ee.geckosolutions.demo.service.dto.PersonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Captor
    private ArgumentCaptor<List<Person>> personListArgumentCaptor;

    @Test
    void testThatSaveAllIsSuccessful() {
        // given
        PersonDTO personDTO = PersonDTO.builder().name("Name").title("Title").imageUrl("ImageUrl").build();

        given(personRepository.saveAll(personListArgumentCaptor.capture())).willReturn(null);

        // when
        personService.saveAll(Collections.singletonList(personDTO));

        // then
        List<Person> personList = personListArgumentCaptor.getValue();
        assertThat(personList.size(), is(equalTo(1)));

        Person person = personList.get(0);
        assertThat(person.getName(), is(equalTo("Name")));
        assertThat(person.getTitle(), is(equalTo("Title")));
        assertThat(person.getImageUrl(), is(equalTo("ImageUrl")));
        assertThat(person.isImageStoredLocally(), is(equalTo(false)));
    }
}
