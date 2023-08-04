package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayFare;
import nextstep.subway.domain.SubwayMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayFareTest {
    private static final int 신분당선_추가요금 = 900;
    private static final int 기본요금 = 1250;

    @DisplayName("이용 거리가 10km 이내인 경우 기본운임 요금을 반환한다")
    @Test
    void calculateBasicFare() {
        // when
        int distance = 10;
        int fare = SubwayFare.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("이용 거리가 10km 초과 ~ 50km 이내인 경우 5km 마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over10_Upto50() {
        // when
        int fare = SubwayFare.calculateFare(16);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("이용 거리가 50km 초과 시 8km마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over50() {
        // when
        int fare = SubwayFare.calculateFare(58);

        // then
        assertThat(fare).isEqualTo(2350);
    }

    @DisplayName("요금계산 시 거리가 0보다 작은 경우 에러가 발생한다")
    @Test
    void calculateFare_InvalidDistance_Exception() {
        // when, then
        assertThatThrownBy(() -> SubwayFare.calculateFare(-1))
                .isInstanceOf(RuntimeException.class)
                .message().isEqualTo("거리가 올바르지 않습니다.");
    }

    @DisplayName("요금계산 시 추가요금이 있는 노선인 경우 요금이 추가된다")
    @Test
    void calculateFare_surchargeLine() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        신분당선.addSection(강남역, 양재역, 8, 5);

        SubwayMap subwayMap = new SubwayMap(Arrays.asList(신분당선));
        Path path = subwayMap.findPath(강남역, 양재역, PathType.DISTANCE);

        // when
        int fare = SubwayFare.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(기본요금 + 신분당선_추가요금);
    }
}
