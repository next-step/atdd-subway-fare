package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private Path path = new Path();

    @DisplayName("경로조회 9km 의 추가금액은 0")
    @Test
    void calculateOverFare_1() {
        int overFare = path.calculateOverFare(9);

        assertThat(overFare).isEqualTo(0);
    }

    @DisplayName("경로조회 10km 의 추가금액은 0")
    @Test
    void calculateOverFare_2() {
        int overFare = path.calculateOverFare(10);

        assertThat(overFare).isEqualTo(0);
    }

    @DisplayName("경로조회 12km 의 추가금액은 100")
    @Test
    void calculateOverFare_3() {
        int overFare = path.calculateOverFare(12);

        assertThat(overFare).isEqualTo(100);
    }

    @DisplayName("경로조회 15km 의 추가금액은 100")
    @Test
    void calculateOverFare_4() {
        int overFare = path.calculateOverFare(15);

        assertThat(overFare).isEqualTo(100);
    }

    @DisplayName("경로조회 16km 의 추가금액은 200")
    @Test
    void calculateOverFare_5() {
        int overFare = path.calculateOverFare(16);

        assertThat(overFare).isEqualTo(200);
    }
}