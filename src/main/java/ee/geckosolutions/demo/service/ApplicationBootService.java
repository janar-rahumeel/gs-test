package ee.geckosolutions.demo.service;

import java.io.File;
import java.io.IOException;

import ee.geckosolutions.demo.domain.Sector;
import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.dto.SectorDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationBootService {

    private final SectorService sectorService;
    private final SectorRepository sectorRepository;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void extractAndStoreSectors() {
        try {
            deleteExistingSectors();
            loadAndStoreSectors();
        } catch (Exception e) {
            log.error("Unable to load sectors", e);
        }
    }

    private void deleteExistingSectors() {
        sectorRepository.deleteAll();
    }

    private void loadAndStoreSectors() throws IOException {
        File exampleFile = new ClassPathResource("/example.html", getClass()).getFile();
        sectorService.readAndParseSectors(exampleFile).values().stream().map(this::map).forEach(sectorRepository::save);
    }

    private Sector map(SectorDTO sectorDTO) {
        Sector sector = new Sector();
        sector.setId(sectorDTO.getId());
        sector.setName(sectorDTO.getName());
        if (sectorDTO.getParent() != null) {
            sector.setParentId(sectorDTO.getParent().getId());
        }
        return sector;
    }

}
