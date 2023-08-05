package nextstep.subway.domain;

import nextstep.member.domain.Member;

public interface FareCalculationRule {
    int calculateFare(Path path, Member member, int totalFare);

    void setNextRule(FareCalculationRule nextRule);
}
