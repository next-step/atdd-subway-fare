package nextstep.subway.domain.service.aggregation;

import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineSection;
import nextstep.subway.domain.service.aggregation.StationLineSectionMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StationPathAggregationService {

    public List<StationLine> getPathStationLine(List<StationLine> stationLines, List<Long> pathStationIds) {
        final List<StationLineSection> pathStationLineSection = getPathStationLineSections(stationLines, pathStationIds);

        return pathStationLineSection.stream()
                .map(StationLineSection::getLine)
                .collect(Collectors.toList());
    }

    public List<StationLineSection> getPathStationLineSections(List<StationLine> stationLines, List<Long> pathStationIds) {
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
        final Map<Long, List<StationLineSection>> sectionMapByUpStation = sections.stream()
                .collect(Collectors.groupingBy(StationLineSection::getUpStationId, Collectors.toList()));

        return sectionMapByUpStation.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> StationLineSectionMap.of(entry.getValue(), StationLineSection::getDownStationId)));
    }
}
