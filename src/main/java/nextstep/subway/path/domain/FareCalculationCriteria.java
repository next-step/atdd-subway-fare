package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.LoginMember;

import java.util.List;

public class FareCalculationCriteria {
    private int distance;
    private List<Line> lines;
    private LoginMember member;

    public FareCalculationCriteria(PathResult pathResult, LoginMember member) {
        this.distance = pathResult.getTotalDistance();
        this.lines = pathResult.getSections().getLinesIncluded();
        this.member = member;
    }

    public int getDistance() {
        return distance;
    }

    public List<Line> getLines() {
        return lines;
    }

    public LoginMember getMember() {
        return member;
    }
}
