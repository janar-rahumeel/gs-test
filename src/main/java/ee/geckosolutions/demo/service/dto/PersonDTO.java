package ee.geckosolutions.demo.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDTO {

    private final String name;
    private final String title;
    private final String imageUrl;

}
