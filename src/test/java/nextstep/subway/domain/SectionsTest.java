package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SectionsTest {

    private Sections sections = new Sections();

    @DisplayName("경로조회 9km 의 추가금액은 0")
    @Test
    void calculateOverFare_1() {
        int fare = sections.calculateOverFare(9);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("경로조회 10km 의 추가금액은 0")
    @Test
    void calculateOverFare_2() {
        int fare = sections.calculateOverFare(10);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("경로조회 12km 의 추가금액은 100")
    @Test
    void calculateOverFare_3() {
        int fare = sections.calculateOverFare(12);

        assertThat(fare).isEqualTo(100);
    }

    @DisplayName("경로조회 15km 의 추가금액은 100")
    @Test
    void calculateOverFare_4() {
        int fare = sections.calculateOverFare(15);

        assertThat(fare).isEqualTo(100);
    }

    @DisplayName("경로조회 16km 의 추가금액은 200")
    @Test
    void calculateOverFare_5() {
        int fare = sections.calculateOverFare(16);

        assertThat(fare).isEqualTo(200);
    }
}