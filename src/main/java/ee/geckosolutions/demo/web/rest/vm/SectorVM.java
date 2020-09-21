package ee.geckosolutions.demo.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

import ee.geckosolutions.demo.domain.Sector;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = "id")
public class SectorVM {

    private final Long id;
    private Long parentId;
    private String name;
    @Builder.Default
    private List<SectorVM> children = new ArrayList<>();

    public static SectorVM of(Sector sector) {
        return SectorVM.builder().id(sector.getId()).parentId(sector.getParentId()).name(sector.getName()).build();
    }

}
