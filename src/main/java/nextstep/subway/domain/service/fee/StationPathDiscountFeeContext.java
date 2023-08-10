package nextstep.subway.domain.service.fee;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
@Builder
public class StationPathDiscountFeeContext {
    private Member member;
}
