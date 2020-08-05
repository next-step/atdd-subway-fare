package nextstep.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

public class DiscountPolicyTypeTest {

    @EnumSource(DiscountPolicyType.class)
    @ParameterizedTest
    void 할인_정책을_가져온다(DiscountPolicyType policyType) {
        DiscountPolicy discountPolicy = policyType.getDiscountPolicy();
        assertThat(discountPolicy).isNotNull();
    }

    @CsvSource({"6, CHILDREN", "12, CHILDREN", "13, YOUTH", "18, YOUTH", "19, ADULT"})
    @ParameterizedTest
    void 나이를_판단한다(int age, DiscountPolicyType expectedType) {
        DiscountPolicyType discountPolicyType = DiscountPolicyType.ofAge(age);
        assertThat(discountPolicyType).isEqualTo(expectedType);
    }

    @CsvSource({"-1, 0, 5"})
    @ParameterizedTest
    void 잘못된_나이가_계산되면_오류를_반환한다(int age) {
        assertThatThrownBy(
            () -> DiscountPolicyType.ofAge(age)
        ).isInstanceOf(IllegalStateException.class).hasMessage("wrong age typed");
    }
}
