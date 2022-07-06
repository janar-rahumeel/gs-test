package ee.geckosolutions.demo.service.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.Cookie;

import ee.geckosolutions.demo.TestApp;
import ee.geckosolutions.demo.domain.Quiz;
import ee.geckosolutions.demo.domain.Sector;
import ee.geckosolutions.demo.repository.QuizRepository;
import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.QuizService;
import ee.geckosolutions.demo.service.SectorService;
import ee.geckosolutions.demo.web.rest.vm.QuizVM;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
@WithUnauthenticatedMockUser
public class QuizResourceIT {

    @Autowired
    private QuizService quizService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void testThatSectorsHasRetrievedSuccessfully() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/quiz/sectors").accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @Transactional
    public void testThatQuizHasBadRequest() throws Exception {
        // given
        Set<Long> sectors = Collections.singleton(1000L);
        QuizVM quizVm = QuizVM.builder().name("Test").agreedToTerms(true).sectors(sectors).build();
        Cookie cookie = new Cookie("JSESSIONID", "F7H4EJFKG4G3v");

        // when
        mockMvc.perform(
                post("/api/quiz").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(quizVm))
                        .with(csrf())
                        .cookie(cookie))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(
                        content().string(
                                "{\"entityName\":\"quiz\",\"errorKey\":\"invalid-sector\",\"type\":\"https://www.jhipster.tech/problem/problem-with-message\",\"title\":\"Invalid sector(s)!\",\"status\":400,\"message\":\"error.invalid-sector\",\"params\":\"quiz\"}"));
    }

    @Test
    @Transactional
    public void testThatQuizHasCreated() throws Exception {
        // given
        Sector sector1 = Sector.builder().id(6L).name("Sector").build();
        sectorRepository.save(sector1);

        Set<Long> sectors = Collections.singleton(6L);
        QuizVM quizVm = QuizVM.builder().name("Test").agreedToTerms(true).sectors(sectors).build();
        Cookie cookie = new Cookie("JSESSIONID", "F7H4EJFKG4G3v");

        // when
        mockMvc.perform(
                post("/api/quiz").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(quizVm))
                        .with(csrf())
                        .cookie(cookie))

                // then
                .andExpect(status().isCreated())
                .andExpect(
                        content().string(
                                "{\"id\":\"b18dac660dccc04f0849a7af47721858\",\"name\":\"Test\",\"agreedToTerms\":true,\"sectors\":[6]}"))
                .andExpect(header().string("location", "/api/quiz/b18dac660dccc04f0849a7af47721858"));
    }

    @Test
    @Transactional
    public void testThatQuizHasUpdated() throws Exception {
        // given
        Sector sector1 = Sector.builder().id(19L).name("Sector Before").build();
        Sector sector2 = Sector.builder().id(6L).name("Sector After").build();
        sectorRepository.saveAll(Arrays.asList(sector1, sector2));

        String sessionId = "testSessionId";
        Set<Long> sectors = Collections.singleton(19L);
        Quiz quiz = Quiz.builder().id(sessionId).agreedToTerms(true).name("Name Before").sectors(sectors).build();
        quizRepository.save(quiz);

        Set<Long> differentSectors = Collections.singleton(6L);
        QuizVM quizVm = QuizVM.builder().id(sessionId).name("Name After").agreedToTerms(true).sectors(differentSectors).build();

        // when
        mockMvc.perform(
                put("/api/quiz").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(quizVm))
                        .with(csrf()))

                // then
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "{\"id\":\"testSessionId\",\"name\":\"Name After\",\"agreedToTerms\":true,\"sectors\":[6]}"));
    }

    @Test
    @Transactional
    public void testThatQuizHasNotFound() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/quiz/non-existing-session-id").accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testThatQuizHasFound() throws Exception {
        // given
        Set<Long> sectors = Collections.singleton(19L);
        Quiz quiz = Quiz.builder().id("existing-session-id").agreedToTerms(true).name("Test").sectors(sectors).build();
        quizRepository.save(quiz);

        // when
        mockMvc.perform(get("/api/quiz/existing-session-id").accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(
                        content().string(
                                "{\"id\":\"existing-session-id\",\"name\":\"Test\",\"agreedToTerms\":true,\"sectors\":[19]}"));
    }

}
