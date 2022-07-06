package ee.geckosolutions.demo.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import ee.geckosolutions.demo.service.dto.PersonDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CsvFileService {

    public List<PersonDTO> readAllPeople(Class<?> resourceClass, String resourceName) {
        List<PersonDTO> personList = new ArrayList<>();
        URL url = Objects.requireNonNull(resourceClass.getResource(resourceName), "No resource found: " + resourceName);
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI())); CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                PersonDTO personDTO = resolvePerson(record);
                personList.add(personDTO);
            }
        } catch (Exception e) {
            throw new CsvFileServiceException("Unable to read people from CSV file", e);
        }
        return personList;
    }

    private PersonDTO resolvePerson(String[] record) {
        if (StringUtils.isBlank(record[2])) {
            return PersonDTO.builder().name(record[0].trim()).imageUrl(record[1].trim()).build();
        }
        return PersonDTO.builder().name(record[0].trim()).title(record[1].trim()).imageUrl(record[2].trim()).build();
    }

}
