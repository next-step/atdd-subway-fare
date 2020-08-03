package nextstep.subway.maps.fare.domain.policy;

import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.fare.utils.FareTestUtils;
import nextstep.subway.members.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountByAgeFarePolicyTest {
    @DisplayName("청소년 요금 할인 테스트")
    @Test
    public void calculateYouthMember() {
        // given
        MemberResponse member = new MemberResponse(1L, "email@email.com", 15);
        FareContext fareContext = new FareContext(FareTestUtils.sampleSubwayPath(), member);
        DiscountByAgeFarePolicy discountByAgeFarePolicy = new DiscountByAgeFarePolicy();

        // when
        discountByAgeFarePolicy.calculate(fareContext);

        // then
        assertThat(fareContext.getFare().getValue()).isEqualTo(720);
    }

    @DisplayName("어린이 요금 할인 테스트")
    @Test
    public void calculateChildrenMember() {
        // given
        MemberResponse member = new MemberResponse(1L, "email@email.com", 10);
        FareContext fareContext = new FareContext(FareTestUtils.sampleSubwayPath(), member);
        DiscountByAgeFarePolicy discountByAgeFarePolicy = new DiscountByAgeFarePolicy();

        // when
        discountByAgeFarePolicy.calculate(fareContext);

        // then
        assertThat(fareContext.getFare().getValue()).isEqualTo(450);
    }
}