package nextstep.subway.path.domain.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.path.domain.rule.AgePolicyRule.KID;
import static nextstep.subway.path.domain.rule.AgePolicyRule.YOUTH;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.FIRST_RULE;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.SECOND_RULE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금정책 테스트")
class FarePolicesTest {

    FarePolices farePolices;

    @BeforeEach
    void setUp(){
        farePolices = new FarePolices();
    }

    @DisplayName("요금계산 테스트 : 기본요금")
    @Test
    void 기본요금(){
        기본요금_정책_적용됨();
        assertThat(farePolices.calculateFare()).isEqualTo(1250);
    }

    @ParameterizedTest(name = "요금계산 테스트 : 거리정책")
    @CsvSource({
            "10, 1250"
            ,"11, 1350"
            ,"50, 2050"
            ,"51, 2150"
            ,"59, 2250"
    })
    void 요금계산_테스트_거리정책(int distance, int expectedFare){
        // when
        기본요금_정책_적용됨();
        거리_정책_적용됨(distance);

        // then
        assertThat(farePolices.calculateFare()).isEqualTo(expectedFare);
    }

    @ParameterizedTest(name = "요금계산 테스트 : 거리정책 + 노선정책")
    @CsvSource({
            "10, 1250"
            ,"11, 1350"
            ,"50, 2050"
            ,"51, 2150"
            ,"59, 2250"
    })
    void 요금계산_테스트_거리정책_노선정책(int distance, int expectedFare){
        // given
        int extraFare = 900;

        // when
        기본요금_정책_적용됨();
        거리_정책_적용됨(distance);
        노선_정책_적용됨(extraFare);

        // then
        assertThat(farePolices.calculateFare()).isEqualTo(expectedFare + extraFare);
    }

    @ParameterizedTest(name = "요금계산 테스트 : 거리정책 + 노선정책 + 연령정책")
    @CsvSource({
            "10, 1250"
            ,"11, 1350"
            ,"50, 2050"
            ,"51, 2150"
            ,"59, 2250"
    })
    void 요금계산_테스트_거리정책_노선정책_연령정책(int distance, int expectedFare){
        // given
        int extraFare = 900;
        int age = 15;

        // when
        기본요금_정책_적용됨();
        거리_정책_적용됨(distance);
        노선_정책_적용됨(extraFare);
        연령_정책_적용됨(age);

        // then
        assertThat(farePolices.calculateFare()).isEqualTo( (int) Math.floor((expectedFare + extraFare - 350) * 0.8) );
    }

    private void 기본요금_정책_적용됨() {
        farePolices.addFarePolicy(new BasePolicy());
    }

    private void 거리_정책_적용됨(int distance) {
        farePolices.addFarePolicy(new DistancePolicy(FIRST_RULE, distance));
        farePolices.addFarePolicy(new DistancePolicy(SECOND_RULE, distance));
    }

    private void 노선_정책_적용됨(int extraFare) {
        farePolices.addFarePolicy(new LinePolicy(extraFare));
    }

    private void 연령_정책_적용됨(int age) {
        farePolices.addFarePolicy(new AgePolicy(KID, age));
        farePolices.addFarePolicy(new AgePolicy(YOUTH, age));
    }

}