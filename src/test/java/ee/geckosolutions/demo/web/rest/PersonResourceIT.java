package ee.geckosolutions.demo.web.rest;

import ee.geckosolutions.demo.test.AbstractWebApplicationIntegrationTest;
import ee.geckosolutions.demo.test.TestPage;
import ee.geckosolutions.demo.web.rest.vm.PageableVM;
import ee.geckosolutions.demo.web.rest.vm.PersonVM;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class PersonResourceIT extends AbstractWebApplicationIntegrationTest {

    @Test
    void testThatFindAllIsSuccessful() {
        // given
        PageableVM pageableVM = PageableVM.builder().pageNumber(0).pageSize(10).build();
        String partialName = "simpso";

        // when
        TestPage<PersonVM> testPage = testRestTemplate.postForEntity("/person/find/name/" + partialName, pageableVM, TestPage.class).getBody();

        // then
        assertThat(testPage.getTotalElements(), is(equalTo(60L)));
        assertThat(testPage.getTotalPages(), is(equalTo(6)));
        assertThat(testPage.getPageSize(), is(equalTo(10)));
    }

}
