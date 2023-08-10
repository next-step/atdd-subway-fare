package nextstep.subway.domain.service.aggregation;

import lombok.AllArgsConstructor;
import nextstep.subway.domain.StationLineSection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StationLineSectionMap {
    final Map<Long, StationLineSection> sectionMap;

    public StationLineSection getSectionBy(Long stationId) {
        return sectionMap.get(stationId);
    }

    public static StationLineSectionMap of(List<StationLineSection> sections, Function<StationLineSection, Long> keySelector) {

        final Map<Long, StationLineSection> sectionMap = sections.stream()
                .collect(Collectors.toMap(keySelector, Function.identity()));

        return new StationLineSectionMap(sectionMap);

    }
}
