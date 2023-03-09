package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource({
            "9,1_250",
            "10,1_250",
            "11,1_350",

            "49,2_050",
            "50,2_050",
            "51,2_150",
    })
    void calculateTotalFare(int distance, int fare) {
        assertThat(DistanceFarePolicy.calculate(distance)).isEqualTo(fare);
    }

    /**
     * calculateExtraFare() 메서드의 문서화를 위한 테스트 코드 작성
     *
     * 추가 운임에 대한 거리에 따른 계산 메서드
     *
     * @param start 시작 위치
     * @param end   도착 위치
     * @param unit  추가 운임의 단위거리
     * @return 거리에 따른 추가운임
     */
    @ParameterizedTest
    @CsvSource({
            "10,14,5, 100",
            "10,15,5, 100",
            "10,16,5, 200",

            "50,57,8, 100",
            "50,58,8, 100",
            "50,59,8, 200",
    })
    void calculateExtraFare(int start, int end, int unit, int expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = DistanceFarePolicy.class.getDeclaredMethod("calculateExtraFare", int.class, int.class, int.class);
        method.setAccessible(true);

        int result = (int) method.invoke(null,start, end, unit);

        assertThat(result).isEqualTo(expected);
    }
}
