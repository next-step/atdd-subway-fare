package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.BaseFarePolicy;
import nextstep.subway.domain.BaseMemberFarePolicy;
import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.MemberFarePolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.domain.BaseFarePolicy.BASE_FARE;
import static nextstep.subway.domain.BaseMemberFarePolicy.EXEMPTION_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyTest {
    @Test
    void 기본_요금_정책_중_거리_10km_이내는_기본_운임_요금이_발생한다() {
        FarePolicy farePolicy = new BaseFarePolicy();

        int amount = farePolicy.calculate(10);

        assertThat(amount).isEqualTo(BASE_FARE);
    }

    @CsvSource(value = {"12,1350", "16,1450", "50,2050"}, delimiter = ',')
    @ParameterizedTest
    void 기본_요금_정책_중_거리_10km_초과_50km_이내는_5km_당_100원_추가_운임_요금이_발생한다(int distance, int fareAmount) {
        FarePolicy farePolicy = new BaseFarePolicy();

        int amount = farePolicy.calculate(distance);

        assertThat(amount).isEqualTo(fareAmount);
    }

    @CsvSource(value = {"50,2050", "51,2150", "58,2250"}, delimiter = ',')
    @ParameterizedTest
    void 기본_요금_정책_중_거리_50km_초과는_8km_당_100원_추가_운임_요금이_발생한다(int distance, int fareAmount) {
        FarePolicy farePolicy = new BaseFarePolicy();

        int amount = farePolicy.calculate(distance);

        assertThat(amount).isEqualTo(fareAmount);
    }

    @Test
    void 어린이_회원은_350원_공제된_금액에_지하철_운임_요금의_50퍼_할인된다() {
        //given
        MemberFarePolicy memberFarePolicy = new BaseMemberFarePolicy(new BaseFarePolicy());
        //when
        int amount = memberFarePolicy.calculate(어린이_회원_생성(), 10);
        //then
        assertThat(amount).isEqualTo((int) ((BASE_FARE - EXEMPTION_AMOUNT) * 0.5));
    }

    @Test
    void 청소년_회원은_350원_공제된_금액에_지하철_운임_요금의_20퍼_할인된다() {
        //given
        MemberFarePolicy memberFarePolicy = new BaseMemberFarePolicy(new BaseFarePolicy());
        //when
        int amount = memberFarePolicy.calculate(청소년_회원_생성(), 10);
        //then
        assertThat(amount).isEqualTo((int) ((BASE_FARE - EXEMPTION_AMOUNT) * 0.8));
    }

    private static Member 어린이_회원_생성() {
        return new Member("children@atdd.com", "atdd", 6);
    }

    private static Member 청소년_회원_생성() {
        return new Member("teenager@atdd.com", "atdd", 13);
    }
}
