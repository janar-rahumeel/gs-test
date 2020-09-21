package ee.geckosolutions.demo.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.common.base.Joiner;

@Converter
public class SectorsConverter implements AttributeConverter<Set<Long>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Long> list) {
        return Joiner.on(';').join(list);
    }

    @Override
    public Set<Long> convertToEntityAttribute(String joined) {
        return Arrays.stream(joined.split(";")).map(Long::valueOf).collect(Collectors.toSet());
    }

}
