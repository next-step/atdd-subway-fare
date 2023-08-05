package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StationPathAccumulateService {
    private final StationLineRepository stationLineRepository;

    public BigDecimal accumulateTotalDistance(List<Long> pathStationIds) {
        if (CollectionUtils.isEmpty(pathStationIds)) {
            return BigDecimal.ZERO;
        }

        final List<StationLine> stationLines = stationLineRepository.findAll();
        final List<StationLineSection> stationLineSections = getPathStationLineSections(stationLines, pathStationIds);

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionByUpStationId(stationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDistance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Long accumulateTotalDuration(List<Long> pathStationIds) {
        if (CollectionUtils.isEmpty(pathStationIds)) {
            return 0L;
        }

        final List<StationLine> stationLines = stationLineRepository.findAll();
        final List<StationLineSection> stationLineSections = getPathStationLineSections(stationLines, pathStationIds);

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionByUpStationId(stationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDuration)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private List<StationLineSection> getPathStationLineSections(List<StationLine> stationLines, List<Long> pathStationIds) {
        return IntStream.range(0, pathStationIds.size() - 1)
                .mapToObj(stationIndex -> {
                    Long stationId = pathStationIds.get(stationIndex);
                    Long neighborStationId = pathStationIds.get(stationIndex + 1);

                    return getSectionByPairStationId(stationLines, stationId, neighborStationId);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private StationLineSection getSectionByPairStationId(List<StationLine> stationLines, Long stationId, Long neighborStationId) {
        final List<StationLineSection> sections = StationLineSection.of(stationLines);
        final Map<Long, StationLineSectionMap> sectionMapByDownStation = getSectionMapByDownStation(sections);
        final Map<Long, StationLineSectionMap> sectionMapByUpStation = getSectionMapByUpStation(sections);

        return Optional.ofNullable(stationId)
                .map(sectionMapByDownStation::get)
                .map(sectionByDownStation -> sectionByDownStation.getSectionBy(neighborStationId))
                .orElse(Optional.ofNullable(stationId)
                        .map(sectionMapByUpStation::get)
                        .map(sectionByUpStation -> sectionByUpStation.getSectionBy(neighborStationId))
                        .orElse(null));
    }

    private Map<Long, StationLineSectionMap> getSectionMapByDownStation(List<StationLineSection> sections) {
        final Map<Long, List<StationLineSection>> sectionMapByDownStation = sections.stream()
                .collect(Collectors.groupingBy(StationLineSection::getDownStationId, Collectors.toList()));

        return sectionMapByDownStation.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> StationLineSectionMap.of(entry.getValue(), StationLineSection::getUpStationId)));
    }

    private Map<Long, StationLineSectionMap> getSectionMapByUpStation(List<StationLineSection> sections) {
        final Map<Long, List<StationLineSection>> sectionMapByDownStation = sections.stream()
                .collect(Collectors.groupingBy(StationLineSection::getUpStationId, Collectors.toList()));

        return sectionMapByDownStation.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> StationLineSectionMap.of(entry.getValue(), StationLineSection::getDownStationId)));
    }

    private Map<Long, StationLineSection> getStationLineSectionByUpStationId(List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(stationLineSections)) {
            return Collections.emptyMap();
        }

        return stationLineSections.stream()
                .collect(Collectors.toMap(StationLineSection::getUpStationId, Function.identity()));
    }
}
