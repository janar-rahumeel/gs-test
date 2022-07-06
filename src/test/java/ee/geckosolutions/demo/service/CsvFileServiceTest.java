package ee.geckosolutions.demo.service;

import ee.geckosolutions.demo.service.dto.PersonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(MockitoExtension.class)
class CsvFileServiceTest {

    @InjectMocks
    private CsvFileService csvFileService;

    @Test
    void testThatReadIsSuccessful() {
        // given
        String resourceName = "/test-people.csv";

        // when
        List<PersonDTO> peopleList = csvFileService.readAllPeople(CsvFileServiceTest.class, resourceName);

        // then
        assertThat(peopleList.size(), is(equalTo(3)));

        PersonDTO personDTO1 = peopleList.get(0);
        assertThat(personDTO1.getName(), is(equalTo("Homer Simpson")));
        assertThat(personDTO1.getTitle(), is(nullValue()));
        assertThat(personDTO1.getImageUrl(), is(equalTo("https://vignette.wikia.nocookie.net/simpsons/images/b/bd/Homer_Simpson.png/revision/latest/scale-to-width-down/72?cb=20140126234206")));

        PersonDTO personDTO2 = peopleList.get(1);
        assertThat(personDTO2.getName(), is(equalTo("Frank Grimes")));
        assertThat(personDTO2.getTitle(), is(equalTo("Jr.")));
        assertThat(personDTO2.getImageUrl(), is(equalTo("https://vignette.wikia.nocookie.net/simpsons/images/2/25/Frank_Grimes_Jr.png/revision/latest/scale-to-width-down/115?cb=20180512222755")));

        PersonDTO personDTO3 = peopleList.get(2);
        assertThat(personDTO3.getName(), is(equalTo("Martin Prince")));
        assertThat(personDTO3.getTitle(), is(equalTo("Sr.")));
        assertThat(personDTO3.getImageUrl(), is(equalTo("https://vignette.wikia.nocookie.net/simpsons/images/f/f1/Martin_Prince%2C_Sr..png/revision/latest/scale-to-width-down/133?cb=20130424150221")));
    }

}
