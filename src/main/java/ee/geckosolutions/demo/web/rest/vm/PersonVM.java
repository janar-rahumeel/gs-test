package ee.geckosolutions.demo.web.rest.vm;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class PersonVM {

    private final Long id;
    private final String name;
    private final String imageUrl;

}
