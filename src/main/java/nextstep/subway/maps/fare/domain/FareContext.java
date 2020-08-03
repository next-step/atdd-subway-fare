package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.dto.MemberResponse;

public class FareContext {
    public static final int DEFAULT_FARE = 1250;
    private final Fare fare;
    private final SubwayPath subwayPath;
    private MemberResponse member;


    public FareContext(SubwayPath subwayPath) {
        this.fare = new Fare(DEFAULT_FARE);
        this.subwayPath = subwayPath;
    }

    public FareContext(SubwayPath sampleSubwayPath, MemberResponse member) {
        this(sampleSubwayPath);
        this.member = member;
    }

    public int getDistance() {
        return this.subwayPath.calculateDistance();
    }

    public int getExtraFare() {
        Line expensiveLine = this.subwayPath.getExpensiveLine();
        return expensiveLine.getExtraFare();
    }

    public Fare getFare() {
        return this.fare;
    }

    public MemberResponse getMember() {
        return this.member;
    }
}
