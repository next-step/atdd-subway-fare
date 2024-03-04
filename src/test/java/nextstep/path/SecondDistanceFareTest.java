package nextstep.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("10km초과∼50km까지 추가요금")
class SecondDistanceFareTest {

    @DisplayName("10km의 추가 요금은 0원")
    @Test
    void overcharge_case0() {

        // given
        int distance = 10;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isZero();
    }

    @DisplayName("11km의 추가 요금은 100원")
    @Test
    void overcharge_case1() {

        // given
        int distance = 11;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(100);
    }


    @DisplayName("11km의 추가 요금은 100원")
    @Test
    void overcharge_case2() {

        // given
        int distance = 11;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(100);
    }


    @DisplayName("16km의 추가 요금은 200원")
    @Test
    void overcharge_case3() {

        // given
        int distance = 16;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(200);
    }


    @DisplayName("20km의 추가 요금은 200원")
    @Test
    void overcharge_case4() {

        // given
        int distance = 20;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(200);
    }

    @DisplayName("50km의 추가 요금은 800원")
    @Test
    void overcharge_case5() {
        // 40 * 20  = 800원
        // given
        int distance = 50;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(800);
    }

    @DisplayName("51km의 추가 요금은 800원")
    @Test
    void overcharge_case6() {
        // 40 * 20  = 800원
        // given
        int distance = 50;
        SecondDistanceFare sut = new SecondDistanceFare();

        // when
        int fare = sut.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(800);
    }

}