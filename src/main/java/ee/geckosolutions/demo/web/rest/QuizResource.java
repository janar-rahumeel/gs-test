package ee.geckosolutions.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import ee.geckosolutions.demo.domain.Quiz;
import ee.geckosolutions.demo.repository.QuizRepository;
import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.QuizService;
import ee.geckosolutions.demo.service.SectorService;
import ee.geckosolutions.demo.web.rest.errors.BadRequestAlertException;
import ee.geckosolutions.demo.web.rest.vm.QuizVM;
import ee.geckosolutions.demo.web.rest.vm.SectorVM;

import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizResource {

    private final QuizService quizService;
    private final SectorService sectorService;
    private final QuizRepository quizRepository;
    private final SectorRepository sectorRepository;

    @GetMapping("/quiz/sectors")
    public ResponseEntity<List<SectorVM>> getAllSectors() {
        return ResponseEntity.ok(sectorService.resolveSectors());
    }

    @GetMapping("/quiz/{sessionId}")
    public ResponseEntity<QuizVM> getQuiz(@PathVariable String sessionId) {
        return ResponseUtil.wrapOrNotFound(quizRepository.findById(sessionId).map(QuizVM::of));
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizVM> createQuiz(
            @CookieValue(name = "JSESSIONID") String sessionId,
            @Valid @RequestBody QuizVM quizVm) throws URISyntaxException {
        log.debug("REST request to create Quiz : {}", quizVm);
        if (quizVm.getId() != null) {
            throw new BadRequestAlertException("Bad request", "quiz", "incorrect-api-usage");
        }
        validateAllSectors(quizVm.getSectors());
        Quiz createdQuiz = quizService.createQuiz(sessionId, quizVm);
        return ResponseEntity.created(new URI("/api/quiz/" + createdQuiz.getId())).body(QuizVM.of(createdQuiz));
    }

    @PutMapping("/quiz")
    public ResponseEntity<QuizVM> updateQuiz(@Valid @RequestBody QuizVM quizVm) {
        log.debug("REST request to update Quiz : {}", quizVm);
        return ResponseUtil.wrapOrNotFound(quizRepository.findById(quizVm.getId()).map(quiz -> {
            validateAllSectors(quizVm.getSectors());
            return QuizVM.of(quizService.updateQuiz(quizVm));
        }));
    }

    private void validateAllSectors(Set<Long> sectors) {
        if (sectors == null) {
            throw new BadRequestAlertException("Invalid sector(s)!", "quiz", "invalid-sector");
        }
        sectors.forEach(
                sectorId -> sectorRepository.findById(sectorId)
                        .orElseThrow(() -> new BadRequestAlertException("Invalid sector(s)!", "quiz", "invalid-sector")));
    }

}
