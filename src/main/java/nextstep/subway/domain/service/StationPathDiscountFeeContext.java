package nextstep.subway.domain.service;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
@Builder
public class StationPathDiscountFeeContext {
    private Member member;
}
