package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Line 의 구간 모음에 대한 테스트")
class SectionsTest {

    @DisplayName("총 금액 기본운임 요금")
    @Test
    void totalFee_basic() {
        //given
        Sections sections = getSections(2, 3, 2);

        //when
        int fee = sections.totalFee();
        //then
        assertThat(fee).isEqualTo(1250);
    }



    @DisplayName("총 금액 기본운임 요금 14KM 기준")
    @Test
    void totalFee_additional_case01() {
        //given
        Sections sections = getSections(5, 5, 4);
        //when
        int fee = sections.totalFee();
        //then
        assertThat(fee).isEqualTo(1350);

    }
    @DisplayName("총 금액 기본운임 요금 15KM 기준")
    @Test
    void totalFee_additional_case02() {
        //given
        Sections sections = getSections(4, 6, 4);
        //when
        int fee = sections.totalFee();
        //then
        assertThat(fee).isEqualTo(1450);

    }
    @DisplayName("총 금액 기본운임 요금 57KM 기준")
    @Test
    void totalFee_additional_case03() {
        //given
        Sections sections = getSections(20, 23, 14);

        //when
        int fee = sections.totalFee();
        //then
        assertThat(fee).isEqualTo(2050);
    }
    @DisplayName("총 금액 기본운임 요금 58KM 기준")
    @Test
    void totalFee_additional_case04() {
        //given
        Sections sections = getSections(20, 20, 18);
        //when
        int fee = sections.totalFee();
        //then
        assertThat(fee).isEqualTo(2150);
    }

    private Sections getSections(int distance1, int distance2, int distance3) {
        return new Sections(Arrays.asList(
                new Section(null,null,null, distance1, 3),
                new Section(null, null, null, distance2, 2),
                new Section(null, null, null, distance3, 2)));
    }
}