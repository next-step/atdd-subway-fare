package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FeePolicyTest {
    FeePolicy feePolicy;

    @BeforeEach
    void setUp() {
        feePolicy = new FeePolicy();
    }

    @DisplayName("총 금액 기본운임 요금")
    @Test
    void totalFee_basic() {
        //when
        int fee = feePolicy.totalFee(7, 0, 20);
        //then
        assertThat(fee).isEqualTo(1250);
    }

    @DisplayName("총 금액 기본운임 요금 15KM 기준")
    @Test
    void totalFee_additional_case01() {
        //when
        int fee = feePolicy.totalFee(15, 0, 20);
        //then
        assertThat(fee).isEqualTo(1250 + 100);

    }
    @DisplayName("총 금액 기본운임 요금 16KM 기준")
    @Test
    void totalFee_additional_case02() {
        //when
        int fee = feePolicy.totalFee(16, 0, 20);
        //then
        assertThat(fee).isEqualTo(1250 + 200);

    }
    @DisplayName("총 금액 기본운임 요금 58KM 기준")
    @Test
    void totalFee_additional_case03() {
        //when
        int fee = feePolicy.totalFee(58, 0, 20);
        //then
        assertThat(fee).isEqualTo(1250 + 900);
    }
    @DisplayName("총 금액 기본운임 요금 59KM 기준")
    @Test
    void totalFee_additional_case04() {
        //when
        int fee = feePolicy.totalFee(59, 0, 20);
        //then
        assertThat(fee).isEqualTo(1250 + 1000);
    }

    @DisplayName("총 금액 및 노선 추가 요금(900원) : 8km")
    @Test
    void totalFee_line_additional_fee_case01() {
        //when
        int fee = feePolicy.totalFee(8, 900, 20);
        //then
        assertThat(fee).isEqualTo(2150);
    }

    @DisplayName("총 금액 및 노선 추가 요금(900원) : 12km")
    @Test
    void totalFee_line_additional_fee_case02() {
        //when
        int fee = feePolicy.totalFee(12, 900, 20);
        //then
        assertThat(fee).isEqualTo(2250);
    }

    @DisplayName("총 금액 테스트, 나이 추가(6세)")
    @Test
    void totalFee_with_age_case01() {
        //when
        int fee = feePolicy.totalFee(8, 900, 6);
        //then
        assertThat(fee).isEqualTo(900);
    }

    @DisplayName("총 금액 테스트, 나이 추가(12세)")
    @Test
    void totalFee_with_age_case02() {
        //when
        int fee = feePolicy.totalFee(8, 900, 12);
        //then
        assertThat(fee).isEqualTo(900);
    }

    @DisplayName("총 금액 테스트, 나이 추가(13세)")
    @Test
    void totalFee_with_age_case03() {
        //when
        int fee = feePolicy.totalFee(8, 900, 13);
        //then
        assertThat(fee).isEqualTo(1440);
    }

    @DisplayName("총 금액 테스트, 나이 추가(18세)")
    @Test
    void totalFee_with_age_case04() {
        //when
        int fee = feePolicy.totalFee(8, 900, 18);
        //then
        assertThat(fee).isEqualTo(1440);
    }


}