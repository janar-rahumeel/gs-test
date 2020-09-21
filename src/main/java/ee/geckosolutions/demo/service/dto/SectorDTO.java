package ee.geckosolutions.demo.service.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorDTO {

    private Long id;
    private String name;
    @Setter
    private SectorDTO parent;
    private final Map<Long, SectorDTO> children = new LinkedHashMap<>();

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public SectorDTO addChild(SectorDTO child) {
        child.setParent(this);
        this.children.put(child.getId(), child);
        return child;
    }

}
