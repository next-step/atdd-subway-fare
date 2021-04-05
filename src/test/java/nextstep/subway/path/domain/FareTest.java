package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.LinePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.path.domain.policy.PolicyConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 테스트")
class FareTest {

    Fare fare;

    @BeforeEach
    void setUp(){
        fare = new Fare();
    }

    @DisplayName("요금계산 테스트 : 기본요금")
    @Test
    void 기본요금(){
        assertThat(fare.getFare()).isEqualTo(1250);
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
        fare.addFarePolicy(new DistancePolicy(D1_MIN_DISTANCE.of(), D1_MAX_DISTANCE.of(), D1_UNIT_DISTANCE.of(), distance));
        fare.addFarePolicy(new DistancePolicy(D2_MIN_DISTANCE.of(), D2_MAX_DISTANCE.of(), D2_UNIT_DISTANCE.of(), distance));

        // then
        assertThat(fare.getFare()).isEqualTo(expectedFare);
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
        fare.addFarePolicy(new DistancePolicy(D1_MIN_DISTANCE.of(), D1_MAX_DISTANCE.of(), D1_UNIT_DISTANCE.of(), distance));
        fare.addFarePolicy(new DistancePolicy(D2_MIN_DISTANCE.of(), D2_MAX_DISTANCE.of(), D2_UNIT_DISTANCE.of(), distance));
        fare.addFarePolicy(new LinePolicy(extraFare));

        // then
        assertThat(fare.getFare()).isEqualTo(expectedFare + extraFare);
    }

}