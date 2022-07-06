package ee.geckosolutions.demo.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestPage<T> {

    private final Page<T> page;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TestPage(@JsonProperty("content") List<T> content,
                    @JsonProperty("pageable") InnerPageable innerPageable,
                    @JsonProperty("totalElements") long total) {
        this.page = new PageImpl<>(content, PageRequest.of(innerPageable.pageNumber, innerPageable.pageSize), total);
    }

    public int getPageSize() {
        return page.getSize();
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Data
    static final class InnerPageable {

        private int pageNumber;
        private int pageSize;

    }

}
