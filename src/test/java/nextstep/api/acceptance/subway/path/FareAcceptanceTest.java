package nextstep.api.acceptance.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import static nextstep.api.acceptance.auth.AuthSteps.일반_로그인_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_생성_성공;
import static nextstep.api.acceptance.subway.line.SectionSteps.지하철구간_등록_성공;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.api.acceptance.AcceptanceTest;
import nextstep.api.acceptance.subway.line.LineSteps;
import nextstep.api.acceptance.subway.station.StationSteps;
import nextstep.api.subway.applicaion.line.dto.request.SectionRequest;
import nextstep.api.subway.domain.path.FareDiscounts;
import nextstep.api.subway.domain.path.FareSections;
import nextstep.api.subway.domain.path.PathSelection;

@DisplayName("지하철 경로 관리 기능")
class FareAcceptanceTest extends AcceptanceTest {

    private Long 교대역, 강남역, 양재역, 남부터미널역, 광교역;

    //  (거리, 소요시간, 추가요금)
    //   교대역    --- (10,10,10) --- 강남역
    //     |                           |
    //  (5,5,5)                     (5,5,5)
    //     |                           |
    // 남부터미널역  ---  (5,5,5)  --- 양재역
    //

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = StationSteps.지하철역_생성_성공("교대역").getId();
        강남역 = StationSteps.지하철역_생성_성공("강남역").getId();
        양재역 = StationSteps.지하철역_생성_성공("양재역").getId();
        남부터미널역 = StationSteps.지하철역_생성_성공("남부터미널역").getId();
        광교역 = StationSteps.지하철역_생성_성공("광교역").getId();

        LineSteps.지하철노선_생성_성공(교대역, 강남역, 10, 10, 10);
        LineSteps.지하철노선_생성_성공(강남역, 양재역, 5, 5, 5);
        지하철구간_등록_성공(LineSteps.지하철노선_생성_성공(교대역, 남부터미널역, 5, 5, 5).getId(), new SectionRequest(남부터미널역, 양재역, 5, 5));
    }

    @Test
    void 최단경로중_추가요금이_있는_노선을_환승할_경우_가장_높은_금액의_추가요금을_적용한다() {
        // when
        final var response = PathSteps.최단경로조회_성공(교대역, 양재역, PathSelection.DISTANCE.name());

        // then
        final var lineAdditionalFare = 5;
        final var expectedTotalFare = FareSections.calculateTotalFare(response.getDistance()) + lineAdditionalFare;
        assertThat(response.getFare()).isEqualTo(expectedTotalFare);
    }

    @Test
    void 로그인_사용자가_청소년이라면_청소년_할인정책을_적용한다() {
        final var age = 13;

        // given
        회원_생성_성공("user@email.com", "password", age);
        final var token = 일반_로그인_성공("user@email.com", "password").getAccessToken();

        // when
        final var response = PathSteps.최단경로조회_성공(token, 교대역, 양재역, PathSelection.DISTANCE.name());

        // then
        final var lineAdditionalFare = 5;
        final var expectedTotalFare = FareDiscounts.discountFareByAge(
                FareSections.calculateTotalFare(response.getDistance()) + lineAdditionalFare, age);
        assertThat(response.getFare()).isEqualTo(expectedTotalFare);
    }

    @Test
    void 로그인_사용자가_어린이라면_청소년_할인정책을_적용한다() {
        final var age = 6;

        // given
        회원_생성_성공("user@email.com", "password", age);
        final var token = 일반_로그인_성공("user@email.com", "password").getAccessToken();

        // when
        final var response = PathSteps.최단경로조회_성공(token, 교대역, 양재역, PathSelection.DISTANCE.name());

        // then
        final var lineAdditionalFare = 5;
        final var expectedTotalFare = FareDiscounts.discountFareByAge(
                FareSections.calculateTotalFare(response.getDistance()) + lineAdditionalFare, age);
        assertThat(response.getFare()).isEqualTo(expectedTotalFare);
    }
}
