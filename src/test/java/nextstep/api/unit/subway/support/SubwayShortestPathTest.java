package nextstep.api.unit.subway.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static nextstep.api.unit.subway.StationFixture.강남역;
import static nextstep.api.unit.subway.StationFixture.교대역;
import static nextstep.api.unit.subway.StationFixture.삼성역;
import static nextstep.api.unit.subway.StationFixture.선릉역;
import static nextstep.api.unit.subway.StationFixture.역삼역;

import java.util.List;

import org.junit.jupiter.api.Test;

import nextstep.api.SubwayException;
import nextstep.api.subway.domain.path.PathSelection;
import nextstep.api.subway.support.SubwayShortestPath;
import nextstep.api.unit.subway.LineFixture;

class SubwayShortestPathTest {

    @Test
    void 출발역부터_도착역까지의_거리기준_최단경로를_반환한다() {
        // 교대역 - 강남역 - 선릉역 - 역삼역 - 삼성역
        final var line = LineFixture.makeLine(교대역, 삼성역, 40, 40);
        LineFixture.appendSection(line, 교대역, 강남역, 10, 10);
        LineFixture.appendSection(line, 선릉역, 삼성역, 20, 20);
        LineFixture.appendSection(line, 선릉역, 역삼역, 10, 10);

        final var path = SubwayShortestPath.builder(line.getStations(), line.getSections())
                .source(line.getFirstStation())
                .target(line.getLastStation())
                .buildOf(PathSelection.DISTANCE);

        final var actual = path.getStation();
        assertThat(actual).containsExactly(교대역, 강남역, 선릉역, 역삼역, 삼성역);
    }

    @Test
    void 출발역부터_도착역까지의_소요시간기준_최단경로를_반환한다() {
        // 교대역 - 강남역 - 선릉역 - 역삼역 - 삼성역
        final var line = LineFixture.makeLine(교대역, 삼성역, 40, 40);
        LineFixture.appendSection(line, 교대역, 강남역, 10, 10);
        LineFixture.appendSection(line, 선릉역, 삼성역, 20, 20);
        LineFixture.appendSection(line, 선릉역, 역삼역, 10, 10);

        final var path = SubwayShortestPath.builder(line.getStations(), line.getSections())
                .source(line.getFirstStation())
                .target(line.getLastStation())
                .buildOf(PathSelection.DURATION);

        final var actual = path.getStation();
        assertThat(actual).containsExactly(교대역, 강남역, 선릉역, 역삼역, 삼성역);
    }

    @Test
    void 출발역부터_도착역까지의_경로를_찾지_못하면_예외가_발생한다() {
        final var line = LineFixture.makeLine(교대역, 삼성역, 40, 10);

        assertThatThrownBy(() ->
                SubwayShortestPath.builder(List.of(교대역, 삼성역, 선릉역), line.getSections())
                        .source(교대역)
                        .target(선릉역)
                        .buildOf(PathSelection.DISTANCE)
        ).isInstanceOf(SubwayException.class);
    }
}