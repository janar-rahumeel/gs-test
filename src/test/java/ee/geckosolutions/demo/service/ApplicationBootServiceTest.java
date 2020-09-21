package ee.geckosolutions.demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import ee.geckosolutions.demo.domain.Sector;
import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.dto.SectorDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@ExtendWith(MockitoExtension.class)
public class ApplicationBootServiceTest {

    @Mock
    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private ApplicationBootService applicationBootService;

    @Test
    public void testThatSectionsAreResetForEachRestart() throws IOException {
        // given
        doNothing().when(sectorRepository).deleteAll();

        File exampleFile = new ClassPathResource("/example.html", ApplicationBootService.class).getFile();
        SectorDTO sectorDTO = SectorDTO.builder().id(1L).build();
        Map<Long, SectorDTO> sectorDTOList = Collections.singletonMap(1L, sectorDTO);
        given(sectorService.readAndParseSectors(exampleFile)).willReturn(sectorDTOList);

        // when
        applicationBootService.extractAndStoreSectors();

        // then
        ArgumentCaptor<Sector> sectorArgumentCaptor = ArgumentCaptor.forClass(Sector.class);
        verify(sectorRepository).save(sectorArgumentCaptor.capture());
        Sector sector = sectorArgumentCaptor.getValue();
        assertThat(sector.getId(), is(equalTo(1L)));
    }

}
