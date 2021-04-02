package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.LinePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    private static final int DEFAULT_FARE = 1250;

    @Test
    @DisplayName("10km 이내일 경우 기본 요금")
    void 기본요금_라인추가요금없음() {
        기본거리_테스트(0, DEFAULT_FARE);
    }

    @DisplayName("10km 이내일 경우 기본 요금, 라인 추가 요금 발생한 경우")
    @ParameterizedTest
    @CsvSource(value = {"300,1550", "500,1750", "100,1350"}, delimiter = ',')
    void 기본요금_라인추가요금(int lineFare, int expectedFare) {
        기본거리_테스트(lineFare, expectedFare);
    }

    @ParameterizedTest
    @DisplayName("11km ~ 50km 이내, 라인 추가 요금 없음")
    @CsvSource(value = {"13, 1350", "22,1550", "35,1750", "40,1850"}, delimiter = ',')
    void 기본_거리요금추가_라인추가요금없음(int distance, int resultFare) {
        기본거리요금추가_테스트(distance, 0, resultFare);
    }

    @ParameterizedTest
    @DisplayName("11km ~ 50km 이내, 라인에 추가 요금 발생한 경우")
    @CsvSource(value = {"12,900,2250", "22,400,1950", "35,600,2350", "40,800,2650"}, delimiter = ',')
    void 기본_거리요금추가_라인추가요금(int distance, int addLineFare, int resultFare) {
        기본거리요금추가_테스트(distance, addLineFare, resultFare);
    }


    @ParameterizedTest
    @DisplayName("거리가 50km 초과되었으며, 라인에 추가 요금 발생한 경우")
    @CsvSource(value = {"52,400,2650", "59,300,2750", "69,700,3450", "74,900,3750"}, delimiter = ',')
    void 기본최대거리초과_라인추가요금(int distance, int addLineFare, int resultFare) {
        Fare fare = new Fare();

        DistancePolicy.OverFiftyDistancePolicy distancePolicy = new DistancePolicy.OverFiftyDistancePolicy(distance);
        LinePolicy.LineFarePolicy lineFarePolicy = new LinePolicy.LineFarePolicy(addLineFare);

        fare.addAllFarePolicy(distancePolicy, lineFarePolicy);
        int calculated = fare.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }

    void 기본거리_테스트(int lineFare, int resultFare) {
        Fare fare = new Fare();

        DistancePolicy.DefaultDistancePolicy distancePolicy = new DistancePolicy.DefaultDistancePolicy();
        LinePolicy.LineFarePolicy linePolicy = new LinePolicy.LineFarePolicy(lineFare);

        fare.addAllFarePolicy(distancePolicy, linePolicy);
        int calculated = fare.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }

    void 기본거리요금추가_테스트(int distance, int addLineFare, int resultFare) {
        Fare fare = new Fare();

        DistancePolicy.OverTenDistancePolicy distancePolicy = new DistancePolicy.OverTenDistancePolicy(distance);
        LinePolicy.LineFarePolicy linePolicy = new LinePolicy.LineFarePolicy(addLineFare);

        fare.addAllFarePolicy(distancePolicy, linePolicy);
        int calculated = fare.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }
}
