package nextstep.subway.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.controller.dto.StationResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum PathType {
    DISTANCE(Section::distance, (stations, value) -> PathResponse.distanceOf(StationResponse.listOf(stations), value)),
    DURATION(Section::duration, (stations, value) -> PathResponse.durationOf(StationResponse.listOf(stations), value)),
    OTHER(section -> 0L, (stations, value) -> PathResponse.otherOf(StationResponse.listOf(stations)));

    private Function<Section, Long> type;
    private BiFunction<List<Station>, Integer, PathResponse> response;

}
