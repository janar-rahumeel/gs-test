package ee.geckosolutions.demo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ee.geckosolutions.demo.domain.Sector;
import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.dto.SectorDTO;
import ee.geckosolutions.demo.web.rest.vm.SectorVM;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@ExtendWith(MockitoExtension.class)
public class SectorServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorService sectorService;

    @Test
    public void testThatWhenFileDoesNotExistExceptionIsTriggered() {
        // given
        File file = new File("not-exist.html");

        // when
        Assertions.assertThrows(RuntimeException.class, () -> sectorService.readAndParseSectors(file));
    }

    @Test
    public void testThatSectorsHasParsedSuccessfully() throws IOException {
        // given
        File file = new ClassPathResource("example.html", SectorService.class.getClassLoader()).getFile();

        // when
        Map<Long, SectorDTO> sectors = sectorService.readAndParseSectors(file);

        // then
        assertThat(sectors, is(notNullValue()));
        assertThat(sectors.size(), is(equalTo(79)));
        SectorDTO sectorFoodAndBeverage = sectors.get(1L).getChildren().get(6L);
        assertThat(sectorFoodAndBeverage.getName(), is(equalTo("\u00a0\u00a0\u00a0\u00a0Food and Beverage")));
        assertThat(sectorFoodAndBeverage.getChildren().size(), is(equalTo(7)));
    }

    @Test
    public void testThatSectorsHasResolvedSuccessfully() {
        // given
        Sector sector1 = Sector.builder().id(1L).name("Root").build();
        Sector sector2 = Sector.builder().id(2L).name("Root 1 Child 1").parentId(1L).build();
        Sector sector3 = Sector.builder().id(3L).name("Root 1 Child 2").parentId(1L).build();
        Sector sector4 = Sector.builder().id(4L).name("Root 1 Child 2 Child 1").parentId(3L).build();
        List<Sector> databaseSectors = Arrays.asList(sector1, sector2, sector3, sector4);
        given(sectorRepository.findAll()).willReturn(databaseSectors);

        // when
        List<SectorVM> sectors = sectorService.resolveSectors();

        // then
        assertThat(sectors, is(notNullValue()));
        assertThat(sectors.size(), is(equalTo(4)));
        SectorVM sectorRoot = sectors.get(0);
        assertThat(sectorRoot.getName(), is(equalTo("Root")));
        SectorVM sectorRoot1Child2 = sectors.get(2);
        assertThat(sectorRoot1Child2.getName(), is(equalTo("Root 1 Child 2")));
    }

}
