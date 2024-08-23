package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.CalculateMemberAgeFare;
import nextstep.path.dto.Path;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CalculateMemberAgeFareTest {

    private Member 사용자_5세;
    private Member 사용자_8세;
    private Member 사용자_18세;
    private Member 사용자_20세;

    private Station 강남역;
    private Station 역삼역;
    private Section 강남역_역삼역_구간;
    private Sections 신분당선_구간 = new Sections();
    private Line 신분당선;
    private Long 기본_노선_추가요금 = 0L;
    private Long 거리_10이하 = 10L;
    private Long 시간_기본 = 5L;
    private Long 노선_거리_기본 = 10L;

    @BeforeEach
    void setup() {
        사용자_5세 = Member.of(1L, "test@test", "password", 5);
        사용자_8세 = Member.of(2L, "test@test", "password", 8);
        사용자_18세 = Member.of(3L, "test@test", "password", 18);
        사용자_20세 = Member.of(4L, "test@test", "password", 20);

        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");

        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 거리_10이하, 시간_기본);
        신분당선_구간.addSection(강남역_역삼역_구간);
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 기본_노선_추가요금);
    }

    @Test
    @DisplayName("회원 정보가 없을 경우 총 요금 그대로 반환")
    void calculateMemberAge_NoMember_ReturnsOriginalPrice() {
        // given
        var 기존_요금 = 1250L;

        // when
        var 계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(null, 기존_요금);

        // then
        assertAll(
                () -> assertThat(계산된_요금).isEqualTo(기존_요금)
        );
    }

    @Test
    @DisplayName("요금이 할인 기준 미만일 경우 할인 없이 그대로 반환")
    void calculateMemberAge_UnderMinimumPriceForDiscount_ReturnsOriginalPrice() {
        // given
        var 기존_요금 = 300L;

        // when
        Long 계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(사용자_18세, 기존_요금);

        // then
        assertAll(
                () -> assertThat(계산된_요금).isEqualTo(기존_요금)
        );
    }

    @Test
    @DisplayName("나이가 6세 미만이거나 19세 이상인 경우 할인 없이 그대로 반환")
    void calculateMemberAge_AgeOutsideDiscountRange_ReturnsOriginalPrice() {
        // given
        var 기존_요금 = 1250L;

        // when
        Long 사용자_5세_계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(사용자_5세, 기존_요금);
        Long 사용자_20세_계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(사용자_20세, 기존_요금);

        // then
        assertAll(
                () -> assertThat(사용자_5세_계산된_요금).isEqualTo(기존_요금),
                () -> assertThat(사용자_20세_계산된_요금).isEqualTo(기존_요금)
        );
    }

    @Test
    @DisplayName("나이가 6세 이상 13세 미만인 경우 50% 할인 적용")
    void calculateMemberAge_AgeBetween6And13_50PercentDiscountApplied() {
        // given
        var 기존_요금 = 1250L;

        // when
        var 계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(사용자_8세, 기존_요금);

        // then
        var 예상_요금 = 기존_요금 - (long) ((기존_요금 - 350) * 0.5);
        assertAll(
                () -> assertThat(계산된_요금).isEqualTo(예상_요금)
        );
    }

    @Test
    @DisplayName("나이가 13세 이상 18세 이하인 경우 20% 할인 적용")
    void calculateMemberAge_AgeBetween13And18_20PercentDiscountApplied() {
        // given
        var 기존_요금 = 1250L;

        // when
        var 계산된_요금 = CalculateMemberAgeFare.calculateMemberAge(사용자_18세, 기존_요금);

        // then
        var 예상_요금 = 기존_요금 - (long) ((기존_요금 - 350) * 0.2);
        assertThat(계산된_요금).isEqualTo(예상_요금);
    }

    @Test
    @DisplayName("경로에 대해 회원 나이에 따른 할인을 적용한 요금 반환")
    void of_ReturnsPathWithCalculatedAgeFare() {
        // given
        var 기존_요금 = 500L;
        var path = Path.of(사용자_8세, List.of(신분당선), List.of(강남역, 역삼역), 신분당선_구간);
        path.setTotalPrice(기존_요금);

        // when
        var 계산된_path = CalculateMemberAgeFare.of(path);

        // then
        var 예상_요금 = 기존_요금 - (long) ((기존_요금 - 350) * 0.5);
        assertAll(
                () -> assertThat(계산된_path.getTotalPrice()).isEqualTo(예상_요금)
        );
    }
}

