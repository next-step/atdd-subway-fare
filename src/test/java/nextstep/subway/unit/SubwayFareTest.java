package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.SubwayFare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayFareTest {
    private static final int 신분당선_추가요금 = 900;
    private static final int 이호선_추가요금 = 0;
    private static final int 삼호선_추가요금 = 300;
    private static final int 기본요금 = 1250;
    private Path path;
    private Station 양재역;
    private Station 강남역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Member 게스트_사용자;
    private Member 청소년_사용자;
    private Member 어린이_사용자;
    private Member 성인_사용자;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        이호선 = new Line("이호선", "bg-green-500", 이호선_추가요금);
        삼호선 = new Line("삼호선", "bg-blue-600", 삼호선_추가요금);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        게스트_사용자 = new Member("guest", "password", 0);
        청소년_사용자 = new Member("teen", "password", 15);
        어린이_사용자 = new Member("children", "password", 6);
        성인_사용자 = new Member("adult", "", 25);
    }

    @DisplayName("거리별 정책 - 이용 거리가 10km 이내인 경우 기본운임 요금을 반환한다. (추가요금 0원)")
    @Test
    void calculateBasicFare() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(surcharge).isEqualTo(1250);
    }

    @DisplayName("거리별 정책 - 이용 거리가 10km 초과 ~ 50km 이내인 경우 5km 마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over10_Upto50() {
        // given
        int distance = 16;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(surcharge).isEqualTo(1450);
    }

    @DisplayName("거리별 정책 - 이용 거리가 50km 초과 시 8km마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over50() {
        // given
        int distance = 58;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(surcharge).isEqualTo(2150);
    }

    @DisplayName("요금계산 시 거리가 0보다 작은 경우 에러가 발생한다")
    @Test
    void calculateDistanceFare_InvalidDistance_Exception() {
        // given
        int distance = -1;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when, then
        assertThatThrownBy(() -> SubwayFare.calculateFare(path, 게스트_사용자))
                .isInstanceOf(RuntimeException.class)
                .message().isEqualTo("거리가 올바르지 않습니다.");
    }

    @DisplayName("노선별 정책 - 요금계산 시 추가요금이 있는 노선인 경우 추가요금을 반환한다")
    @Test
    void calculateLineFare() {
        // given
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 8, 5);
        path = createPath(강남역_양재역_구간);

        // when
        int lineSurcharge = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(lineSurcharge).isEqualTo(기본요금 + 신분당선_추가요금);
    }

    @DisplayName("노선별 정책 - 요금계산 시 추가요금이 있는 노선이 여러개인 경우 가장 높은 금액의 추가요금을 반환한다")
    @Test
    void calculateLineFare_MaxSurcharge() {
        // given
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 3, 5);
        Section 남부터미널_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 5, 3);
        path = createPath(강남역_양재역_구간, 남부터미널_양재역_구간);

        // when
        int lineSurcharge = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(lineSurcharge).isEqualTo(기본요금 + 신분당선_추가요금);
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 청소년(13세 이상 ~ 19세 미만)인 경우 운임에서 350원을 공제한 금액의 20%를 할인한다")
    @Test
    void calculateAgeFare_Teenager() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = SubwayFare.calculateFare(path, 청소년_사용자);

        // then
        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 어린이(6세 이상 ~ 13세 미만)인 경우 운임에서 350원을 공제한 금액의 50%를 할인한다")
    @Test
    void calculateAgeFare_Children() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = SubwayFare.calculateFare(path, 어린이_사용자);

        // then
        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 성인(19세 이상)인 경우 할인되지 않는다")
    @Test
    void calculateAgeFare_Adult() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = SubwayFare.calculateFare(path, 성인_사용자);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    private Path createPath(Section... section) {
        Sections sections = new Sections(List.of(section));
        return new Path(sections);
    }
}
