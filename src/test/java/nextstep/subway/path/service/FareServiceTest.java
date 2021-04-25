package nextstep.subway.path.service;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareServiceTest {

    private FareService fareService;

    private LoginMember loginMember;

    private final static int DEFAULT_FARE = 1_250;

    @BeforeEach
    public void setUp() {
        this.fareService = new FareService();
        loginMember = new LoginMember(1L, "gpwls", "1234", 100);
    }

    @DisplayName("거리가 10 이하일때 기본 요금임을 확인")
    @Test
    public void defaultFare() {
        int fare = fareService.calculate(10, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인")
    @Test
    public void over10KmFare() {
        int fare = fareService.calculate(12, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인2")
    @Test
    public void over10KmFare2() {
        int fare = fareService.calculate(16, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 200);
    }

    @DisplayName("거리가 50 초과 일때 요금 확인")
    @Test
    public void over50KmFare() {
        int fare = fareService.calculate(51, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE + 600);
    }

    @DisplayName("거리가 10이하인데 추가요금 부과 확인")
    @Test
    public void defaultFareWithAdditionalFee() {
        int fare = fareService.calculate(10, 2000, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE+2000);
    }

    @DisplayName("거리가 10이하에 청소년 할인 확인")
    @Test
    public void defaultFareWithTeenagerDiscount() {
        loginMember = new LoginMember(1L, "gpwls", "1234", 15);
        int fare = fareService.calculate(10, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE - ((DEFAULT_FARE - 350) * 20 / 100));
    }

    @DisplayName("거리가 10이하에 어린이 할인 확인")
    @Test
    public void defaultFareWithChildDiscount() {
        loginMember = new LoginMember(1L, "gpwls", "1234", 10);
        int fare = fareService.calculate(10, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE - ((DEFAULT_FARE - 350) * 50 / 100));
    }

    @DisplayName("Anonymous 사용자인 경우 할인 불가.")
    @Test
    public void defaultFareWithAnonymousUer() {
        loginMember = new LoginMember(1L, "", "", 10);
        int fare = fareService.calculate(10, 0, loginMember).getCost();

        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }
}
