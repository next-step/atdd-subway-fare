package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private Path path = new Path();

    @DisplayName("경로조회 9km 의 추가금액은 0")
    @Test
    void calculateOverFare_1() {
        int overFare = path.getOverFare(9);

        assertThat(overFare).isEqualTo(0);
    }

    @DisplayName("경로조회 10km 의 추가금액은 0")
    @Test
    void calculateOverFare_2() {
        int overFare = path.getOverFare(10);

        assertThat(overFare).isEqualTo(0);
    }

    @DisplayName("경로조회 12km 의 추가금액은 100")
    @Test
    void calculateOverFare_3() {
        int overFare = path.getOverFare(12);

        assertThat(overFare).isEqualTo(100);
    }

    @DisplayName("경로조회 15km 의 추가금액은 100")
    @Test
    void calculateOverFare_4() {
        int overFare = path.getOverFare(15);

        assertThat(overFare).isEqualTo(100);
    }

    @DisplayName("경로조회 16km 의 추가금액은 200")
    @Test
    void calculateOverFare_5() {
        int overFare = path.getOverFare(16);

        assertThat(overFare).isEqualTo(200);
    }

    @DisplayName("경로조회 50km 의 추가금액은 800")
    @Test
    void calculateOverFare_6() {
        int overFare = path.getOverFare(50);

        assertThat(overFare).isEqualTo(800);
    }

    @DisplayName("경로조회 58km 의 추가금액은 800")
    @Test
    void calculateOverFare_7() {
        int overFare = path.getOverFare(58);

        assertThat(overFare).isEqualTo(900);
    }

    @DisplayName("경로조회 59km 의 추가금액은 1600")
    @Test
    void calculateOverFare_8() {
        int overFare = path.getOverFare(59);

        assertThat(overFare).isEqualTo(1000);
    }

}