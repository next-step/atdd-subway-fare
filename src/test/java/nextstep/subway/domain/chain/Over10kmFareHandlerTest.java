package nextstep.subway.domain.chain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({Over10kmFareHandler.class})
class Over10kmFareHandlerTest {

    @Autowired
    private Over10kmFareHandler over10kmFareHandler;

    @ParameterizedTest
    @CsvSource({"11, 100", "16, 200", "21, 300", "26, 400", "31, 500", "36, 600", "41, 700", "46, 800", "50, 800"})
    void 이용거리가_10km_초과_50km_이하에서_5km마다_요금이_100원씩_증가한다(long distance, long price){
        // when
        long fare = over10kmFareHandler.calculate(distance);

        // then
        assertThat(fare).isEqualTo(price);
    }

}
