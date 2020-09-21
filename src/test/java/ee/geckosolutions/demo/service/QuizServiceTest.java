package ee.geckosolutions.demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import ee.geckosolutions.demo.domain.Quiz;
import ee.geckosolutions.demo.repository.QuizRepository;
import ee.geckosolutions.demo.web.rest.vm.QuizVM;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.util.DigestUtils;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    public void testThatQuizHasCreated() {
        // given
        String sessionId = "testSessionId";
        QuizVM quizVm = QuizVM.builder().id(sessionId).name("Test").build();

        doAnswer((Answer<Quiz>) invocation -> (Quiz) invocation.getArguments()[0]).when(quizRepository).save(any(Quiz.class));

        // when
        Quiz quiz = quizService.createQuiz(sessionId, quizVm);

        // then
        String transformedSessionId = DigestUtils.md5DigestAsHex(sessionId.getBytes());
        assertThat(quiz.getId(), is(equalTo(transformedSessionId)));
    }

    @Test
    public void testThatQuizHasUpdated() {
        // given
        String sessionId = "testSessionId";
        Set<Long> sectors = Collections.singleton(23L);
        Quiz quiz = Quiz.builder().id(sessionId).name("Name Before").agreedToTerms(true).sectors(sectors).build();
        given(quizRepository.findById(sessionId)).willReturn(Optional.of(quiz));

        QuizVM quizVm = QuizVM.builder().id(sessionId).name("Name After").agreedToTerms(true).sectors(sectors).build();

        // when
        Quiz updatedQuiz = quizService.updateQuiz(quizVm);

        // then
        assertThat(updatedQuiz.getName(), is(equalTo("Name After")));
    }

}
