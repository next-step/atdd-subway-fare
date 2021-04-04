package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.AgeDiscountPolicy;
import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.FarePolicy;
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
    void 기본요금_라인추가요금(int addLineFare, int resultFare) {
        기본거리_테스트(addLineFare, resultFare);
    }

    @DisplayName("10km 이내일 경우 기본 요금, 연령별 요금 할인")
    @ParameterizedTest
    @CsvSource(value = {"17,1070", "6,800", "19,1250"}, delimiter = ',')
    void 기본요금_라인추가요금없음_연령별할인(int age, int resultFare) {
        DistancePolicy.DefaultDistancePolicy distancePolicy = new DistancePolicy.DefaultDistancePolicy();
        FarePolicy ageDiscountPolicy = AgeDiscountPolicy.getAgeDiscountPolicy(age);

        FarePolicies farePolicies = FarePolicies.of(distancePolicy, ageDiscountPolicy);
        int calculated = farePolicies.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }

    @DisplayName("10km 이내일 경우 기본 요금, 라인 추가 요금, 연령별 요금 할인")
    @ParameterizedTest
    @CsvSource(value = {"3,6,500,1050", "7,13,900,1790", "8,17,100,1150", "9,19,1000,2250"}, delimiter = ',')
    void 기본요금_라인추가요금_연령별할인(int distance, int age, int addLineFare, int resultFare) {
        라인추가_연령별할인_테스트(distance, addLineFare, resultFare, age);
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
    @DisplayName("11km ~ 50km 이내, 라인에 추가 요금 발생한 경우, 연령별할인")
    @CsvSource(value = {"12,900,2250,5", "22,400,1630,14", "35,600,1950,13", "40,800,2650,19"}, delimiter = ',')
    void 기본_거리요금추가_라인추가요금_연령별할인(int distance, int addLineFare, int resultFare, int age) {
        라인추가_연령별할인_테스트(distance, addLineFare, resultFare, age);
    }

    @ParameterizedTest
    @DisplayName("거리가 50km 초과되었으며, 라인에 추가 요금 발생한 경우")
    @CsvSource(value = {"52,500,2650", "59,500,2750", "69,700,3150", "74,900,3450"}, delimiter = ',')
    void 기본최대거리초과_라인추가요금(int distance, int addLineFare, int resultFare) {
        기본거리요금추가_테스트(distance, addLineFare, resultFare);
    }

    @ParameterizedTest
    @DisplayName("거리가 50km 초과되었으며, 라인에 추가 요금 발생한 경우, 연령별할인")
    @CsvSource(value = {"52,500,1500,6", "59,500,1550,10", "69,700,2510,15", "74,900,2670,17"}, delimiter = ',')
    void 기본최대거리초과_라인추가요금_연령별할인(int distance, int addLineFare, int resultFare, int age) {
        라인추가_연령별할인_테스트(distance, addLineFare, resultFare, age);
    }

    void 기본거리_테스트(int addLineFare, int resultFare) {
        DistancePolicy.DefaultDistancePolicy distancePolicy = new DistancePolicy.DefaultDistancePolicy();
        LinePolicy.LineFarePolicy linePolicy = new LinePolicy.LineFarePolicy(addLineFare);

        FarePolicies farePolicies = FarePolicies.of(distancePolicy, linePolicy);
        int calculated = farePolicies.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }

    void 기본거리요금추가_테스트(int distance, int addLineFare, int resultFare) {
        DistancePolicy.OverTenDistancePolicy distancePolicy = new DistancePolicy.OverTenDistancePolicy(distance);
        LinePolicy.LineFarePolicy linePolicy = new LinePolicy.LineFarePolicy(addLineFare);

        FarePolicies farePolicies = FarePolicies.of(distancePolicy, linePolicy);
        int calculated = farePolicies.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }

    void 라인추가_연령별할인_테스트(int distance, int addLineFare, int resultFare, int age) {
        FarePolicy distancePolicy = DistancePolicy.getDistancePolicy(distance);
        LinePolicy.LineFarePolicy lineFarePolicy = new LinePolicy.LineFarePolicy(addLineFare);
        FarePolicy ageDiscountPolicy = AgeDiscountPolicy.getAgeDiscountPolicy(age);

        FarePolicies farePolicies = FarePolicies.of(distancePolicy, lineFarePolicy, ageDiscountPolicy);
        int calculated = farePolicies.calculate(DEFAULT_FARE);

        assertThat(calculated).isEqualTo(resultFare);
    }
}
