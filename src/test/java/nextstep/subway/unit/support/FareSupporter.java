package nextstep.subway.unit.support;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.domain.MemberAge;
import nextstep.subway.domain.path.DistanceFarePolicy;

public class FareSupporter {

    public static int 운임_요금_계산(int 최대_추가_요금, int 총_경로_거리, MemberAge 회원_나이) {
        return new DistanceFarePolicy(최대_추가_요금).calculateFare(총_경로_거리, 회원_나이);
    }

    public static void 계산된_요금과_일치한다(int totalPrice, int expected) {
        assertThat(totalPrice).isEqualTo(expected);
    }
}
