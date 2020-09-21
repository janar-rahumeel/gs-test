package ee.geckosolutions.demo.service;

import ee.geckosolutions.demo.domain.Quiz;
import ee.geckosolutions.demo.repository.QuizRepository;
import ee.geckosolutions.demo.web.rest.vm.QuizVM;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz createQuiz(String sessionId, QuizVM quizVm) {
        quizVm.setId(DigestUtils.md5DigestAsHex(sessionId.getBytes()));
        Quiz quiz = map(quizVm);
        return quizRepository.save(quiz);
    }

    private Quiz map(QuizVM quizVm) {
        return Quiz.builder()
                .id(quizVm.getId().trim())
                .name(quizVm.getName().trim())
                .agreedToTerms(quizVm.isAgreedToTerms())
                .sectors(quizVm.getSectors())
                .build();
    }

    public Quiz updateQuiz(QuizVM quizVm) {
        Quiz quiz = quizRepository.findById(quizVm.getId().trim())
                .orElseThrow(() -> new RuntimeException("Not found: " + quizVm.getId()));
        quiz.setName(quizVm.getName().trim());
        quiz.setAgreedToTerms(quizVm.isAgreedToTerms());
        quiz.setSectors(quizVm.getSectors());
        return quiz;
    }

}
