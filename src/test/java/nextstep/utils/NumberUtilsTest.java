package nextstep.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.utils.NumberUtils.*;
import static org.assertj.core.api.Assertions.*;

class NumberUtilsTest {

    @Test
    @DisplayName("양수인 경우 입력받은 값을 반환한다.")
    void requirePositiveNumberTest() {
        assertThat(requirePositiveNumber(1)).isEqualTo(1);
    }

    @ParameterizedTest(name = "[{argumentsWithNames}] 양수가 아닌 경우 예외가 발생한다.")
    @ValueSource(ints = {-1, 0})
    void requirePositiveNumberFailTest(int number) {
        assertThatIllegalArgumentException().isThrownBy(() -> requirePositiveNumber(number));
    }
}