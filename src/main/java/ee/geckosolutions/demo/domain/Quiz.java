package ee.geckosolutions.demo.domain;

import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "quiz")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotEmpty
    @Convert(converter = SectorsConverter.class)
    private Set<Long> sectors;

    @NotNull
    private boolean agreedToTerms;

}
