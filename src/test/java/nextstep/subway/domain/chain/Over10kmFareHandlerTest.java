package nextstep.subway.domain.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Over10kmFareHandlerTest {

    private Over10kmFareHandler over10kmFareHandler;

    @BeforeEach
    void setUp(){
        over10kmFareHandler = new Over10kmFareHandler();
    }

    @ParameterizedTest
    @CsvSource({"11, 100", "16, 200", "21, 300", "26, 400", "31, 500", "36, 600", "41, 700", "46, 800", "50, 800"})
    void 이용거리가_10km_초과_50km_이하에서_5km마다_요금이_100원씩_증가한다(long distance, long price){
        // when
        long fare = over10kmFareHandler.calculate(distance);

        // then
        assertThat(fare).isEqualTo(price);
    }

}
