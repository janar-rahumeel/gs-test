package ee.geckosolutions.demo.web.rest.vm;

import java.util.Set;

import ee.geckosolutions.demo.domain.Quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizVM {

    private String id;

    private String name;

    private boolean agreedToTerms;

    private Set<Long> sectors;

    public static QuizVM of(Quiz quiz) {
        return QuizVM.builder()
                .id(quiz.getId())
                .name(quiz.getName())
                .agreedToTerms(quiz.isAgreedToTerms())
                .sectors(quiz.getSectors())
                .build();
    }

}
