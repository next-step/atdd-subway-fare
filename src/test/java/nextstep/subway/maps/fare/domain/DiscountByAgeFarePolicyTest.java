package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.fare.utils.FareTestUtils;
import nextstep.subway.members.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountByAgeFarePolicyTest {
    @DisplayName("청소년 요금 할인 테스트")
    @Test
    public void calculateYouthMember() {
        // given
        Member member = new Member("email@email.com", "pasSw0rb", 15);
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
        Member member = new Member("email@email.com", "pasSw0rb", 10);
        FareContext fareContext = new FareContext(FareTestUtils.sampleSubwayPath(), member);
        DiscountByAgeFarePolicy discountByAgeFarePolicy = new DiscountByAgeFarePolicy();

        // when
        discountByAgeFarePolicy.calculate(fareContext);

        // then
        assertThat(fareContext.getFare().getValue()).isEqualTo(450);
    }
}