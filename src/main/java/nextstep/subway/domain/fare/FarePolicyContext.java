package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.line.Line;

import java.util.List;

public class FarePolicyContext {
    private Long distance;
    private List<Line> lines;
    private Member member;

    public FarePolicyContext() {
    }

    public FarePolicyContext(Long distance) {
        this.distance = distance;
    }

    public FarePolicyContext(List<Line> lines) {
        this.lines = lines;
    }

    public FarePolicyContext(Member member) {
        this.member = member;
    }

    public FarePolicyContext(Long distance, List<Line> lines, Member member) {
        this.distance = distance;
        this.lines = lines;
        this.member = member;
    }

    public Long getdistance() {
        return distance;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Member getMember() {
        return member;
    }
}
