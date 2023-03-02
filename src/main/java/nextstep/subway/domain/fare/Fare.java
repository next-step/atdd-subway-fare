package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class Fare {
    private final Path path;
    private final Member member;
    private final int cost;

    private Fare(Path path, Member member, int cost) {
        this.path = path;
        this.member = member;
        this.cost = cost;
    }

    public static Fare of(Path path, Member member) {
        return new Fare(path, member, 0);
    }

    public Fare withModified(int fare) {
        return new Fare(path, member, fare);
    }

    public Path getPath() {
        return path;
    }

    public Member getMember() {
        return member;
    }

    public int getCost() {
        return cost;
    }
}
