package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {
    int defaultOverFareLine = 0;
    int maxOverFareLine = 900;

    @DisplayName("10km이내로 기본요금")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void basicfare(int distance){
        Fare fare = Fare.builder()
                .distance(distance)
                .overFareLine(defaultOverFareLine)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(1250));
    }

    @DisplayName("예외 케이스 - 거리 0인 경우 에러 발생")
    @Test
    void fareException(){
        assertThatThrownBy(() -> Fare.builder()
                    .distance(0)
                    .overFareLine(defaultOverFareLine)
                    .build().getFare())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("10km이상 50km이내로 기본요금+초과요금 (5km당 100원)")
    @Test
    void overfare(){
        Fare fare = Fare.builder()
                .distance(11)
                .overFareLine(defaultOverFareLine)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(1350));
    }

    @DisplayName("10km이상 50km이내로 기본요금+초과요금 (5km당 100원)")
    @Test
    void overfare2(){
        Fare fare = Fare.builder()
                .distance(50)
                .overFareLine(defaultOverFareLine)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(2050));
    }

    @DisplayName("50km초과로 기본요금+초과요금 (8km당 100원)")
    @Test
    void overfare3(){
        Fare fare = Fare.builder()
                .distance(51)
                .overFareLine(defaultOverFareLine)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(1850));
    }

    @DisplayName("50km초과로 기본요금+초과요금 (8km당 100원)+노선초과요금")
    @Test
    void overfarePlusOverFareLint(){
        Fare fare = Fare.builder()
                .distance(51)
                .overFareLine(maxOverFareLine)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(1850+maxOverFareLine));
    }

    @DisplayName("어린이 할인 적용요금")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void discountChildrenFare(int age){
        Fare fare = Fare.builder()
                .loginStatus(true)
                .distance(10)
                .age(age)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(450));
    }

    @DisplayName("청소년 할인 적용요금")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void discountYouthFare(int age){
        Fare fare = Fare.builder()
                .loginStatus(true)
                .distance(10)
                .age(age)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(720));
    }

    @DisplayName("할인 미적용요금")
    @Test
    void discountYouthFare(){
        Fare fare = Fare.builder()
                .loginStatus(true)
                .distance(10)
                .age(19)
                .build();

        assertThat(fare.getFare()).isEqualTo(new BigDecimal(1250));
    }
}