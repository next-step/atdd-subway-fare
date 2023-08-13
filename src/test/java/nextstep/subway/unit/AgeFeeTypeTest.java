package nextstep.subway.unit;

import nextstep.subway.domain.fee.AgeFeeType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AgeFeeTypeTest {

    @Test
    void 어린이는_6세_이상_13세_미만이다() {
        //then
        Assertions.assertThat(AgeFeeType.getAgeFeeTypeByAge(6)).isEqualTo(AgeFeeType.CHILDREN);
        Assertions.assertThat(AgeFeeType.getAgeFeeTypeByAge(12)).isEqualTo(AgeFeeType.CHILDREN);
    }

    @Test
    void 청소년은_13세_이상_19세_미만이다() {
        //then
        Assertions.assertThat(AgeFeeType.getAgeFeeTypeByAge(13)).isEqualTo(AgeFeeType.TEENAGER);
        Assertions.assertThat(AgeFeeType.getAgeFeeTypeByAge(18)).isEqualTo(AgeFeeType.TEENAGER);
    }

    @Test
    void 성인은_19세_이상이다() {
        //then
        Assertions.assertThat(AgeFeeType.getAgeFeeTypeByAge(19)).isEqualTo(AgeFeeType.ADULT);
    }

    @Test
    void 어린이는_운임에서_350원을_공제한_금액의_50퍼() {
        //then
        Assertions.assertThat(AgeFeeType.CHILDREN.getDiscountFee(1350)).isEqualTo(500);
    }

    @Test
    void 청소년은_운임에서_350원을_공제한_금액의_80퍼() {
        //then
        Assertions.assertThat(AgeFeeType.TEENAGER.getDiscountFee(1350)).isEqualTo(800);
    }

    @Test
    void 성인은_운임에서_100퍼() {
        //then
        Assertions.assertThat(AgeFeeType.ADULT.getDiscountFee(1350)).isEqualTo(1350);
    }

}
