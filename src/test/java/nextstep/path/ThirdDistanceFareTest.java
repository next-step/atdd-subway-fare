package nextstep.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("50km 초과 시 추가요금")
class ThirdDistanceFareTest {

    @DisplayName("50km의 추가 요금은 0원")
    @Test
    void overcharge_case0() {

        // given
        int distance = 50;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isZero();
    }

    @DisplayName("51km의 추가 요금은 100원")
    @Test
    void overcharge_case1() {

        // given
        int distance = 51;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isEqualTo(100);
    }


    @DisplayName("58km의 추가 요금은 100원")
    @Test
    void overcharge_case2() {

        // given
        int distance = 58;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isEqualTo(100);
    }


    @DisplayName("59km의 추가 요금은 200원")
    @Test
    void overcharge_case3() {

        // given
        int distance = 59;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isEqualTo(200);
    }


    @DisplayName("66km의 추가 요금은 200원")
    @Test
    void overcharge_case4() {

        // given
        int distance = 66;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isEqualTo(200);
    }

    @DisplayName("67km의 추가 요금은 300원")
    @Test
    void overcharge_case6() {
        // given
        int distance = 67;
        ThirdDistanceFare sut = new ThirdDistanceFare(distance);

        // when
        int fare = sut.calculateFare();

        // then
        assertThat(fare).isEqualTo(300);
    }
    
}