package nextstep.subway.domain.service.path;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.*;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.service.aggregation.StationPathAggregationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationPathAccumulateService {
    private final StationLineRepository stationLineRepository;
    private final StationPathAggregationService stationPathAggregationService;

    public BigDecimal accumulateTotalDistance(List<Long> pathStationIds) {
        if (CollectionUtils.isEmpty(pathStationIds)) {
            return BigDecimal.ZERO;
        }

        final List<StationLine> stationLines = stationLineRepository.findAll();
        final List<StationLineSection> stationLineSections = stationPathAggregationService.getPathStationLineSections(stationLines, pathStationIds);

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
        final List<StationLineSection> stationLineSections = stationPathAggregationService.getPathStationLineSections(stationLines, pathStationIds);

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionByUpStationId(stationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDuration)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private Map<Long, StationLineSection> getStationLineSectionByUpStationId(List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(stationLineSections)) {
            return Collections.emptyMap();
        }

        return stationLineSections.stream()
                .collect(Collectors.toMap(StationLineSection::getUpStationId, Function.identity()));
    }
}
