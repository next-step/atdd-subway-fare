package nextstep.subway.domain.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({BasicFareHandler.class, Over10kmFareHandler.class, Over50kmFareHandler.class})
class FareHandlerTest {

    private FareHandlerFactory fareHandlerFactory;

    @BeforeEach
    void setUp() {
        fareHandlerFactory = new FareHandlerFactory();
    }

    @ParameterizedTest
    @CsvSource({"9, 1250", "10, 1250"})
    void 이용거리가_10km_이하의_요금을_계산한다(int distance, int expected) {
        long fare = fareHandlerFactory.calculateFare(distance);
        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"11, 1350", "16, 1450", "21, 1550", "26, 1650", "31, 1750", "36, 1850", "41, 1950", "46, 2050", "50, 2050"})
    void 이용거리가_10km_초과_50km_이하의_요금을_계산한다(int distance, int expected) {
        long fare = fareHandlerFactory.calculateFare(distance);
        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"51, 2150", "58, 2150", "59, 2250"})
    void 이용거리가_50km_초과의_요금을_계산한다(int distance, int expected) {
        long fare = fareHandlerFactory.calculateFare(distance);
        assertThat(fare).isEqualTo(expected);
    }

}
