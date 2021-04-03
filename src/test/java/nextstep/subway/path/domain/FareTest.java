package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 테스트")
class FareTest {

    @ParameterizedTest(name = "요금계산 테스트")
    @CsvSource({
                "10, 1250"
                ,"11, 1350"
                ,"50, 2050"
                ,"51, 2150"
                ,"59, 2250"
                })
    void 요금계산_테스트(int distance, int expectedFare){
        Fare fare = new Fare(new Distance(distance));
        Assertions.assertThat(fare.getFare()).isEqualTo(expectedFare);
    }
}