package nextstep.subway.domain.service;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;
import nextstep.subway.domain.StationLine;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class StationPathFeeContext {
    private BigDecimal distance;
    private Member member;
    private List<StationLine> lines;
}
