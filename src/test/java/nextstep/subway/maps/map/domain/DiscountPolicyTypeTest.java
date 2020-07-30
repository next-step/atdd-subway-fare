package nextstep.subway.maps.map.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscountPolicyTypeTest {

    @ParameterizedTest
    @CsvSource({"6,CHILDREN", "12,CHILDREN", "13,YOUTH", "18,YOUTH", "19,ADULT"})
    void ofAge(int age, DiscountPolicyType expectedType) {
        DiscountPolicy discountPolicy = DiscountPolicyType.ofAge(age);

        assertThat(discountPolicy).isEqualTo(expectedType.getDiscountPolicy());
    }

    @ParameterizedTest
    @CsvSource({"-1, 0 , 3"})
    void getDiscountPolicyError(int age) {
        assertThatThrownBy(() -> DiscountPolicyType.ofAge(age))
                .isInstanceOf(RuntimeException.class);
    }

}