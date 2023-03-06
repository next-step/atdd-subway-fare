package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistacneFarePolicyTest {

    @Test
    void 기본_요금_계산() {
        DistacneFarePolicy distacneFarePolicy = DistacneFarePolicy.of(10);
        int amount = distacneFarePolicy.calculate(10);

        assertThat(distacneFarePolicy).isEqualTo(DistacneFarePolicy.DEFAULT);
        assertThat(amount).isEqualTo(1250);
    }

    @Test
    void 일차_추가_구간요금_계산() {
        DistacneFarePolicy distacneFarePolicy = DistacneFarePolicy.of(15);
        int amount = distacneFarePolicy.calculate(15);

        assertThat(distacneFarePolicy).isEqualTo(DistacneFarePolicy.SECTION1);
        assertThat(amount).isEqualTo(1350);
    }

    @Test
    void 이차_추가_구간요금_계산() {
        DistacneFarePolicy distacneFarePolicy = DistacneFarePolicy.of(58);
        int amount = distacneFarePolicy.calculate(58);

        assertThat(distacneFarePolicy).isEqualTo(DistacneFarePolicy.SECTION2);
        assertThat(amount).isEqualTo(1250 + 800 + 100);
    }
}