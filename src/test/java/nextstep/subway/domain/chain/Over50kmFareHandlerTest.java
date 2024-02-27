package nextstep.subway.domain.chain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@Import({Over50kmFareHandler.class})
public class Over50kmFareHandlerTest {

    @Autowired
    private Over50kmFareHandler over50kmFareHandler;

    @ParameterizedTest
    @CsvSource({"51, 100", "58, 100", "59, 200"})
    void 이용거리가_50km_초과시_8km마다_요금이_100원씩_증가한다(long distance, long price){
        // when
        long fare = over50kmFareHandler.calculate(distance);

        // then
        assertThat(fare).isEqualTo(price);
    }

}
