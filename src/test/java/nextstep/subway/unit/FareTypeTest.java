package nextstep.subway.unit;

import nextstep.subway.domain.FareType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FareTypeTest {

    @ParameterizedTest(name = "거리에 따른 부과 기준 [{arguments}]")
    @MethodSource("findFare")
    void findFare(int distance, FareType fareType) {
        //when
        FareType fare = FareType.from(distance);

        //then
        assertThat(fare).isEqualTo(fareType);
    }

    private static Stream<Arguments> findFare() {
        return Stream.of(
                Arguments.of(10, FareType.BASIC),
                Arguments.of(11, FareType.PER_FIVE),
                Arguments.of(50, FareType.PER_FIVE),
                Arguments.of(51, FareType.PER_EIGHT)
        );
    }

    @ParameterizedTest(name = "거리에 따른 요금 [{arguments}]")
    @CsvSource(value = {
            "10, 1250",
            "11, 1350",
            "50, 2150",
            "51, 1850",
    })
    void fare(int distance, int expectedFare) {
        //when
        int fare = FareType.fare(distance);

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

}
