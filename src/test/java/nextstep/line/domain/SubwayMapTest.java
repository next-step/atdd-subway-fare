package nextstep.line.domain;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.exception.ShortPathSameStationException;
import nextstep.exception.StationNotExistException;
import nextstep.line.domain.path.ShortPath;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.line.LineTestField.*;
import static nextstep.line.domain.path.ShortPathType.DISTANCE;
import static nextstep.line.domain.path.ShortPathType.DURATION;
import static nextstep.member.MemberTestUser.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayMapTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Station 수원역 = new Station("수원역");
    private static final Station 노원역 = new Station("노원역");
    private static final Station 대림역 = new Station("대림역");

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private SubwayMap subwayMap;

    @BeforeEach
    void setUp() {
        신분당선 = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, SHINBUNDANG_LINE_SURCHARGE, 강남역, 선릉역, 2, 3);
        이호선 = new Line(TWO_LINE_NAME, TWO_LINE_COLOR, BUNDANG_LINE_SURCHARGE, 선릉역, 수원역, 3, 5);
        삼호선 = new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 1);
        삼호선.addSection(노원역, 수원역, 3, 4);
        subwayMap = new SubwayMap(List.of(신분당선, 이호선, 삼호선));
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 이동거리가 가장 짧은 경로를 리턴해야한다.")
    @Test
    void 강남역_수원역_이동거리_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DISTANCE, 강남역, 수원역, 비회원);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 선릉역, 수원역);
        assertThat(shortPath.getDistance()).isEqualTo(5);
        assertThat(shortPath.getDuration()).isEqualTo(8);
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
            "신분당선 추가요금이 붙은 할인되지 않은 금액이 리턴되야한다.")
    @Test
    void 강남역_수원역_비회원_요금_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DISTANCE, 강남역, 수원역, 비회원);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 선릉역, 수원역);
        assertThat(shortPath.getDistance()).isEqualTo(5);
        assertThat(shortPath.getDuration()).isEqualTo(8);
        assertThat(shortPath.getFare()).isEqualTo(2150);
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
            "신분당선 추가요금이 붙은 할인되지 않은 금액이 리턴되야한다.")
    @Test
    void 강남역_수원역_성인_요금_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DISTANCE, 강남역, 수원역, 성인);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 선릉역, 수원역);
        assertThat(shortPath.getDistance()).isEqualTo(5);
        assertThat(shortPath.getDuration()).isEqualTo(8);
        assertThat(shortPath.getFare()).isEqualTo(2150);
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
            "신분당선 추가요금이 붙은 어린이 할인이 적용된 금액이 리턴되야한다.")
    @Test
    void 강남역_수원역_어린이_요금_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DISTANCE, 강남역, 수원역, 어린이);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 선릉역, 수원역);
        assertThat(shortPath.getDistance()).isEqualTo(5);
        assertThat(shortPath.getDuration()).isEqualTo(8);
        assertThat(shortPath.getFare()).isEqualTo(900);
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
            "신분당선 추가요금이 붙은 청소년 할인이 적용된 금액이 리턴되야한다.")
    @Test
    void 강남역_수원역_청소년_요금_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DISTANCE, 강남역, 수원역, 청소년);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 선릉역, 수원역);
        assertThat(shortPath.getDistance()).isEqualTo(5);
        assertThat(shortPath.getDuration()).isEqualTo(8);
        assertThat(shortPath.getFare()).isEqualTo(1440);
    }

    @DisplayName("강남역에서 수원역으로 가는 경로조회시 소요시간이 가장 짧은 경로를 리턴해야한다.")
    @Test
    void 강남역_수원역_소요시간_검증() {
        // when
        ShortPath shortPath = subwayMap.findShortPath(DURATION, 강남역, 수원역, 비회원);

        // then
        assertThat(shortPath.getStations()).hasSize(3).containsExactly(강남역, 노원역, 수원역);
        assertThat(shortPath.getDuration()).isEqualTo(5);
        assertThat(shortPath.getDistance()).isEqualTo(8);
    }

    @DisplayName("최단경로 조회 역중 노선에 포함되지 않은 역이 존재할 경우 에러를 던진다.")
    @Test
    void 경로조회_미포함역() {
        // given when then
        assertThatThrownBy(() -> subwayMap.findShortPath(DISTANCE, 선릉역, 대림역, 비회원))
                .isExactlyInstanceOf(StationNotExistException.class)
                .hasMessage("노선에 역이 존재하지 않습니다.");
    }

    @DisplayName("최단경로 조회 시작역, 종착역이 동일할 경우 에러를 던진다.")
    @Test
    void 경로조회_시작역_종착역_동일() {
        // given when then
        assertThatThrownBy(() -> subwayMap.findShortPath(DISTANCE, 대림역, 대림역, 비회원))
                .isExactlyInstanceOf(ShortPathSameStationException.class)
                .hasMessage("최단경로 시작역, 종착역이 동일할 수 없습니다.");
    }

}