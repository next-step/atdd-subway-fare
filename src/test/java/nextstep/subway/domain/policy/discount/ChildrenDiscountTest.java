package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Guest;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChildrenDiscountTest {

    private DiscountPolicy childrenDiscount;

    @BeforeEach
    void setUp() {
        childrenDiscount = new ChildrenDiscount();
    }

    @Test
    @DisplayName("어린이 할인 정책은 어린이만 적용된다.")
    void supports() {
        Member children = new Member("member@email.com", "password", 10);
        Member adult = new Member("member@email.com", "password", 20);
        Member guest = new Guest();

        assertAll(() -> {
            assertThat(childrenDiscount.supports(children)).isTrue();
            assertThat(childrenDiscount.supports(adult)).isFalse();
            assertThat(childrenDiscount.supports(guest)).isFalse();
        });
    }
}