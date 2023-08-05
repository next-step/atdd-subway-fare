package nextstep.subway.acceptance;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayFareTest {
    private static final int 신분당선_추가요금 = 900;
    private static final int 삼호선_추가요금 = 300;
    private static final int 기본요금 = 1250;

    @DisplayName("이용 거리가 10km 이내인 경우 기본운임 요금을 반환한다")
    @Test
    void calculateBasicFare() {
        // when
        int distance = 10;
        int fare = SubwayFare.calculateDistanceFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("이용 거리가 10km 초과 ~ 50km 이내인 경우 5km 마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over10_Upto50() {
        // when
        int fare = SubwayFare.calculateDistanceFare(16);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("이용 거리가 50km 초과 시 8km마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over50() {
        // when
        int fare = SubwayFare.calculateDistanceFare(58);

        // then
        assertThat(fare).isEqualTo(2350);
    }

    @DisplayName("요금계산 시 거리가 0보다 작은 경우 에러가 발생한다")
    @Test
    void calculateDistanceFare_InvalidDistance_Exception() {
        // when, then
        assertThatThrownBy(() -> SubwayFare.calculateDistanceFare(-1))
                .isInstanceOf(RuntimeException.class)
                .message().isEqualTo("거리가 올바르지 않습니다.");
    }

    @DisplayName("요금계산 시 추가요금이 있는 노선인 경우 추가요금을 반환한다")
    @Test
    void calculateLineFare() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section section = new Section(신분당선, 강남역, 양재역, 8, 5);

        // when
        int lineSurcharge = SubwayFare.calculateLineFare(new Sections(Arrays.asList(section)));

        // then
        assertThat(lineSurcharge).isEqualTo(신분당선_추가요금);
    }

    @DisplayName("요금계산 시 추가요금이 있는 노선이 여러개인 경우 가장 높은 금액의 추가요금을 반환한다")
    @Test
    void calculateLineFare_MaxSurcharge() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        Line 삼호선 = new Line("삼호선", "bg-blue-600", 삼호선_추가요금);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 8, 5);
        Section 남부터미널_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 5, 3);

        // when
        int lineSurcharge = SubwayFare.calculateLineFare(new Sections(Arrays.asList(강남역_양재역_구간, 남부터미널_양재역_구간)));

        // then
        assertThat(lineSurcharge).isEqualTo(신분당선_추가요금);
    }

    @DisplayName("요금계산 시 로그인 사용자의 연령이 청소년(13세 이상 ~ 19세 미만)인 경우 운임에서 350원을 공제한 금액의 20%를 할인한다")
    @Test
    void calculateAgeFare_Teenager() {
        // given
        Member member = new Member("email", "password", 15);
        int totalFare = 1350;

        // when
        int fare = SubwayFare.calculateAgeFare(member, totalFare);

        // then
        assertThat(fare).isEqualTo(800);
    }

    @DisplayName("요금계산 시 로그인 사용자의 연령이 어린이(6세 이상 ~ 13세 미만)인 경우 운임에서 350원을 공제한 금액의 50%를 할인한다")
    @Test
    void calculateAgeFare_Children() {
        // given
        Member member = new Member("email", "password", 6);
        int totalFare = 1350;

        // when
        int fare = SubwayFare.calculateAgeFare(member, totalFare);

        // then
        assertThat(fare).isEqualTo(500);
    }
}
