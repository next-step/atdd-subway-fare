package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    private final static int DEFAULT_FARE = 1_250;

    @DisplayName("모두 기본요금일 때 추가요금 정상적으로 적용")
    @Test
    void additionalFeeWithDefaultFare() {
        LoginMember loginMember = new LoginMember(1L, "","", 10);
        Fare fare = Fare.createInstance(1_000, 0, loginMember);

        //when
        int fareCost = fare.calculateCost();

        //then
        assertThat(fareCost).isEqualTo(DEFAULT_FARE + 1_000);
    }
}
