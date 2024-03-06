package nextstep.subway.domain;

import java.util.Optional;
import java.util.Set;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.LineResponse;

public interface FareCalculator {
    int calculateFare(int distance, Set<LineResponse> lines, Optional<Member> member);
}
