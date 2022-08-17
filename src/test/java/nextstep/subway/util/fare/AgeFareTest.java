package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.util.fixture.SectionFixture.십킬로미터_이하_구간목록;
import static org.assertj.core.api.Assertions.assertThat;

class AgeFareTest {
    private final AgeFare ageFare = new AgeFare();
    private final Path path = new Path(십킬로미터_이하_구간목록);

    @DisplayName("성인의 지하철 요금을 계산한다.")
    @Test
    void calculateAdultFare() {
        Member 성인 = new Member("email@email.com", "password", 19);

        assertThat(ageFare.calculate(path, 성인)).isEqualTo(1250);
    }

    @DisplayName("어린이의 지하철 요금을 계산한다.")
    @ValueSource(ints = {6, 12})
    @ParameterizedTest
    void calculateChildrenFare(int age) {
        Member 어린이 = new Member("email@email.com", "password", age);

        assertThat(ageFare.calculate(path, 어린이)).isEqualTo(800);
    }

    @DisplayName("청소년의 지하철 요금을 계산한다.")
    @ValueSource(ints = {13, 18})
    @ParameterizedTest
    void calculateTeenagerFare(int age) {
        Member 청소년 = new Member("email@email.com", "password", age);

        assertThat(ageFare.calculate(path, 청소년)).isEqualTo(1070);
    }
}
