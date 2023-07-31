package nextstep.subway.domain.service;

import nextstep.subway.domain.StationLineSection;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StationPathAccumulateService {

    public BigDecimal accumulateTotalDistance(List<Long> pathStationIds, List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(pathStationIds)) {
            return BigDecimal.ZERO;
        }

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionMapByUpStationId(stationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDistance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Long accumulateTotalDuration(List<Long> pathStationIds, List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(stationLineSections)) {
            return 0L;
        }

        final Map<Long, StationLineSection> sectionByUpStationId = getStationLineSectionMapByUpStationId(stationLineSections);

        return pathStationIds.stream()
                .map(sectionByUpStationId::get)
                .filter(Objects::nonNull)
                .map(StationLineSection::getDuration)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private Map<Long, StationLineSection> getStationLineSectionMapByUpStationId(List<StationLineSection> stationLineSections) {
        if (CollectionUtils.isEmpty(stationLineSections)) {
            return Collections.emptyMap();
        }

        return stationLineSections.stream()
                .collect(Collectors.toMap(StationLineSection::getUpStationId, Function.identity()));
    }
}
