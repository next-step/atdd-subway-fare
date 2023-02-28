package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.subway.domain.AgeFarePolicy;
import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("연령 요금 계산 정책 테스트")
class AgeFarePolicyTest {

    private static final String EMAIL = "test@gmail.com";

    @DisplayName("어린이 기본운임료를 계산한다.")
    @ParameterizedTest(name = "총 요금이 1250원인 경우 어린이 나이 {0} 세는 {1} 원의 요금을 낸다.")
    @CsvSource(value = {"6,800", "12,800"})
    void calculateYouthDiscountFare() {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 6);
        final Fare actual = AgeFarePolicy.of(loginMember, Fare.BASE_FARE);

        assertThat(actual).isEqualTo(Fare.from(800));
    }

    @DisplayName("청소년 기본운임료를 계산한다.")
    @ParameterizedTest(name = "총 요금이 1250원인 경우 청소년 나이 {0} 세는 {1} 원의 요금을 낸다.")
    @CsvSource(value = {"13,1070", "18,1070"})
    void calculateChildDiscountFare() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, 13);
        final Fare actual = AgeFarePolicy.of(loginMember, Fare.BASE_FARE);

        assertThat(actual).isEqualTo(Fare.from(1070));
    }

    @DisplayName("성인 기본운임료를 계산한다.")
    @ParameterizedTest(name = "총 요금이 1250원인 경우 성인의 나이 {0} 세부터는 {1} 원의 요금을 낸다.")
    @CsvSource(value = {"19,1250"})
    void calculateAdultFare() {
        final LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 20);
        final Fare actual = AgeFarePolicy.of(loginMember, Fare.BASE_FARE);

        assertThat(actual).isEqualTo(Fare.from(1250));
    }
}
