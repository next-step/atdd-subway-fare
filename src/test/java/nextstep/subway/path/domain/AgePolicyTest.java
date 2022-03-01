package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class AgePolicyTest {

    @ParameterizedTest
    @CsvSource({"6, 2000, 825", "12, 2000, 825", "13, 2000, 1320", "19, 2000, 2000", "0, 2000, 2000"})
    void calculateByAge(int age, int fare, int expectedFare) {
        // given
        Fare firstFare = Fare.from(fare);
        AgePolicy agePolicy = AgePolicy.from(age);

        // when
        Fare resultFare = agePolicy.getFare(firstFare);

        // then
        assertThat(resultFare.getValue()).isEqualTo(expectedFare);
    }
}
