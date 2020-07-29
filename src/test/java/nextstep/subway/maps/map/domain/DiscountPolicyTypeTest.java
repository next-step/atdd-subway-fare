package nextstep.subway.maps.map.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscountPolicyTypeTest {

    @ParameterizedTest
    @EnumSource(DiscountPolicyType.class)
    void getDiscountPolicy(DiscountPolicyType discountPolicyType) {
        DiscountPolicy discountPolicy = discountPolicyType.getDiscountPolicy();

        assertThat(discountPolicy).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"6,CHILDREN", "12,CHILDREN", "13,YOUTH", "18,YOUTH", "19,ADULT"})
    void ofAge(int age, DiscountPolicyType expectedType) {
        DiscountPolicyType discountPolicyType = DiscountPolicyType.ofAge(age);

        assertThat(discountPolicyType).isEqualTo(expectedType);
    }

    @ParameterizedTest
    @CsvSource({"-1, 0 , 3"})
    void getDiscountPolicyError(int age) {
        assertThatThrownBy(() -> DiscountPolicyType.ofAge(age))
                .isInstanceOf(RuntimeException.class);
    }

}