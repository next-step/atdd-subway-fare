package nextstep.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CeilTest {
    @Test
    void ceil() {
        // 파라미터 이상의 값을 dobule 형으로 반환한다. 왜 힌트에 이걸 주셨찌?
        double ceil = Math.ceil(10.4);
        Assertions.assertThat(ceil).isEqualTo(11);
    }
}
