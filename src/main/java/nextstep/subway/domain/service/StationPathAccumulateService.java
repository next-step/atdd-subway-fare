package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationLineSection;
import nextstep.subway.domain.StationLineSections;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StationPathAccumulateService {
    private final StationLineRepository stationLineRepository;

    public BigDecimal accumulateTotalDistance(List<Long> pathStationIds) {
        if (CollectionUtils.isEmpty(pathStationIds)) {
            return BigDecimal.ZERO;
        }

        final List<StationLineSection> pathStationLineSections = getPathStationLineSections(pathStationIds);

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionMapByUpStationId(pathStationLineSections);

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

        final List<StationLineSection> pathStationLineSections = getPathStationLineSections(pathStationIds);

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionMapByUpStationId(pathStationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDuration)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private List<StationLineSection> getPathStationLineSections(List<Long> pathStationIds) {
        final Map<Long, Map<Long, StationLineSection>> sectionByUpStationByDownStation = stationLineRepository.findAll()
                .stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(StationLineSection::getUpStationId, Collectors.toMap(StationLineSection::getDownStationId, Function.identity())));

        final Map<Long, Map<Long, StationLineSection>> sectionByDownStationByUpStation = stationLineRepository.findAll()
                .stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(StationLineSection::getDownStationId, Collectors.toMap(StationLineSection::getUpStationId, Function.identity())));

        return IntStream.range(0, pathStationIds.size() - 1)
                .mapToObj(stationIndex -> {
                    Long stationId = pathStationIds.get(stationIndex);
                    Long nextStationId = pathStationIds.get(stationIndex + 1);

                    return Optional.ofNullable(stationId)
                            .map(sectionByUpStationByDownStation::get)
                            .map(sectionByDownStation -> sectionByDownStation.get(nextStationId))
                            .orElse(Optional.ofNullable(stationId)
                                    .map(sectionByDownStationByUpStation::get)
                                    .map(sectionByUpStation -> sectionByUpStation.get(nextStationId))
                                    .orElse(null));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Map<Long, StationLineSection> getStationLineSectionMapByUpStationId(List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(stationLineSections)) {
            return Collections.emptyMap();
        }

        return stationLineSections.stream()
                .collect(Collectors.toMap(StationLineSection::getUpStationId, Function.identity()));
    }
}
