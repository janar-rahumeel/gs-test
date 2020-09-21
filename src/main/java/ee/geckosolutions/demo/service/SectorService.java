package ee.geckosolutions.demo.service;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import ee.geckosolutions.demo.repository.SectorRepository;
import ee.geckosolutions.demo.service.dto.SectorDTO;
import ee.geckosolutions.demo.web.rest.vm.SectorVM;

import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectorService {

    private static final List<SectorVM> ORDERED_SECTORS = new LinkedList<>();

    private final SectorRepository sectorRepository;

    public List<SectorVM> resolveSectors() {
        if (ORDERED_SECTORS.isEmpty()) {
            Map<Long, SectorVM> result = new LinkedHashMap<>();
            sectorRepository.findAll().forEach(sector -> {
                SectorVM sectorVm = result.computeIfAbsent(sector.getId(), id -> SectorVM.of(sector));
                if (sectorVm.getName() == null) {
                    sectorVm.setName(sector.getName());
                }
                if (sector.getParentId() != null) {
                    if (sectorVm.getParentId() == null) {
                        sectorVm.setParentId(sector.getParentId());
                    }
                    SectorVM parentSectorVM = result
                            .computeIfAbsent(sector.getParentId(), id -> SectorVM.builder().id(id).build());
                    parentSectorVM.getChildren().add(sectorVm);
                }
            });
            result.values().stream().filter(sectorVM -> sectorVM.getParentId() == null).forEach(this::addToCache);
        }
        return ORDERED_SECTORS;
    }

    private void addToCache(SectorVM sectorVm) {
        ORDERED_SECTORS.add(sectorVm);
        sectorVm.getChildren().sort(Comparator.comparing(SectorVM::getName));
        sectorVm.getChildren().forEach(this::addToCache);
        sectorVm.setChildren(null);
    }

    public Map<Long, SectorDTO> readAndParseSectors(File file) {
        try {
            Document document = Jsoup.parse(file, CharsetNames.UTF_8);
            return parseSectorsAndReturnSectors(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Long, SectorDTO> parseSectorsAndReturnSectors(Document document) {
        Map<Long, Pair<Integer, SectorDTO>> sectorsTreeMap = resolveSectorsTreeMap(document);
        sectorsTreeMap.forEach((id, pair) -> {
            Integer level = pair.getKey();
            if (level == 0) {
                populateChildren(pair, sectorsTreeMap);
            }
        });
        return sectorsTreeMap.values().stream().map(Pair::getValue).collect(Collectors.toMap(SectorDTO::getId, entry -> entry));
    }

    private Map<Long, Pair<Integer, SectorDTO>> resolveSectorsTreeMap(Document document) {
        return document.select("option")
                .stream()
                .map(this::resolveLevelAndSectorDTO)
                .collect(
                        Collectors.toMap(
                                entry -> entry.getRight().getId(),
                                Function.identity(),
                                (oldValue, newValue) -> newValue,
                                LinkedHashMap::new));
    }

    private Pair<Integer, SectorDTO> resolveLevelAndSectorDTO(Element element) {
        Long id = Long.valueOf(element.attr("value"));
        SectorDTO sectorDTO = SectorDTO.builder().id(id).name(element.wholeText()).build();
        Integer level = StringUtil.resolveWhiteSpaceIndent(element.wholeText());
        return Pair.of(level, sectorDTO);
    }

    private void populateChildren(Pair<Integer, SectorDTO> parentPair, Map<Long, Pair<Integer, SectorDTO>> sectorsTree) {
        AtomicBoolean reachedToParentNode = new AtomicBoolean(false);
        sectorsTree.forEach((id, pair) -> {
            Long parentId = parentPair.getValue().getId();
            if (parentId.equals(id)) {
                reachedToParentNode.set(true);
                return;
            }
            if (reachedToParentNode.get()) {
                Integer descendantLevel = parentPair.getKey() + 1;
                Integer currentSectorLevel = pair.getKey();
                if (currentSectorLevel.equals(descendantLevel)) {
                    parentPair.getValue().addChild(pair.getValue());
                } else if (currentSectorLevel > descendantLevel) {
                    SectorDTO lastChild = parentPair.getValue()
                            .getChildren()
                            .values()
                            .stream()
                            .reduce((first, second) -> second)
                            .orElseThrow(() -> new IllegalStateException("No children found"));
                    Pair<Integer, SectorDTO> lastDescendantPair = Pair.of(descendantLevel, lastChild);
                    populateChildren(lastDescendantPair, sectorsTree);
                } else {
                    reachedToParentNode.set(false);
                }
            }
        });
    }

}
