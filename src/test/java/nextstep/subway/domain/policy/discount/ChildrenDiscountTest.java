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

    @Test
    @DisplayName("어린이 할인은 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    void discount() {
        final int fare = 1250;
        Member children = new Member("member@email.com", "password", 10);

        assertThat(childrenDiscount.discount(fare)).isEqualTo(800);
    }
}