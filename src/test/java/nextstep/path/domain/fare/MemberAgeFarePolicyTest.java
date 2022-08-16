package nextstep.path.domain.fare;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAgeFarePolicyTest {


    @DisplayName("나이 기반 요금 할인")
    @ParameterizedTest
    @CsvSource({
            "5,1800",
            "12,900",
            "13,1440",
            "19,1800",
    })
    void apply(int age, int expected) {
        Member member = new Member("email@email.com", "password", age);

        int result = new MemberAgeFarePolicy(member).apply(1800);

        assertThat(result).isEqualTo(expected);
    }
}
