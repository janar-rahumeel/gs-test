package ee.geckosolutions.demo;

import ee.geckosolutions.demo.service.CsvFileService;
import ee.geckosolutions.demo.service.PersonService;
import ee.geckosolutions.demo.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestAppListener implements ApplicationListener<ApplicationReadyEvent> {

    private final CsvFileService csvFileService;
    private final PersonService personService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            log.info("Populating person list ...");
            List<PersonDTO> personList = csvFileService.readAllPeople(TestAppListener.class, "/people.csv");
            if (log.isDebugEnabled()) {
                log.debug("{} items found", personList.size());
            }
            personService.saveAll(personList);
        } catch (Exception e) {
            log.error("Unable to populate person list", e);
        }
    }

}
