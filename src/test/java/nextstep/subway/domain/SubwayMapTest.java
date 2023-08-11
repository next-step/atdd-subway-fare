package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.application.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayMapTest {

    Station gyodaeStation;
    Station gangnameStation;
    Station yangjaeStation;
    Station southTerminalStation;
    Line no2Line;
    Line shinbundantLine;
    Line no3Line;
    List<Line> lines;

    @BeforeEach
    void setUp() {
        gyodaeStation = new Station("교대역");
        gangnameStation = new Station("강남역");
        yangjaeStation = new Station("양재역");
        southTerminalStation = new Station("남부터미널역");

        no2Line = new Line("2호선", "green");
        no2Line.addSection(gyodaeStation, gangnameStation, 10, 10);

        shinbundantLine = new Line("신분당선", "red");
        shinbundantLine.addSection(gangnameStation, yangjaeStation, 10, 5);

        no3Line = new Line("3호선", "orange");
        no3Line.addSection(gyodaeStation, southTerminalStation, 2, 20);
        no3Line.addSection(southTerminalStation, yangjaeStation, 3, 2);
        lines = List.of(no3Line, no2Line, shinbundantLine);
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void findPathByDistance() {
        // when
        Path path = SubwayMap.findPath(FindPathType.DISTANCE.name(),gyodaeStation, yangjaeStation,lines);

        // then
        Assertions.assertAll(
                () -> assertThat(path.extractDistance()).isEqualTo(5),
                () -> assertThat(path.getStations()).usingRecursiveComparison()
                        .isEqualTo(
                                List.of(StationResponse.of(gyodaeStation),
                                        StationResponse.of(southTerminalStation),
                                        StationResponse.of(yangjaeStation))
                        )
        );
    }

    @DisplayName("최소 시간 경로 조회")
    @Test
    void findPathByDuration() {
        // when
        Path path = SubwayMap.findPath(FindPathType.DURATION.name(),gyodaeStation, yangjaeStation,lines);

        // then
        Assertions.assertAll(
                () -> assertThat(path.extractDuration()).isEqualTo(15),
                () -> assertThat(path.getStations()).usingRecursiveComparison()
                        .isEqualTo(
                                List.of(StationResponse.of(gyodaeStation),
                                        StationResponse.of(gangnameStation),
                                        StationResponse.of(yangjaeStation))
                        )
        );
    }

    @DisplayName("경로 조회시 추가된 요금중 가장 높은 금액만 기본 요금에 합쳐서 조회된다")
    @Test
    void testOnlyHighestAdditionalFareIsAddedToBaseFare() {
        // given
        no2Line.updateExtraFare(20);
        shinbundantLine.updateExtraFare(30);
        no3Line.updateExtraFare(40);

        // when
        Path path = SubwayMap.findPath(FindPathType.DURATION.name(),gyodaeStation, yangjaeStation,lines);

        // then
        Assertions.assertAll(
                () -> assertThat(path.extractDuration()).isEqualTo(15),
                () -> assertThat(path.getFare()).isEqualTo(430),
                () -> assertThat(path.getStations()).usingRecursiveComparison()
                        .isEqualTo(
                                List.of(StationResponse.of(gyodaeStation),
                                        StationResponse.of(gangnameStation),
                                        StationResponse.of(yangjaeStation))
                        )
        );
    }
}
