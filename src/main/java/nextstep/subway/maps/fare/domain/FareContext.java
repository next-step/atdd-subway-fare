package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.LineStationEdge;
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
        Line expensiveLine = this.subwayPath.getLineStationEdges().stream()
                .map(LineStationEdge::getLine)
                .max((line, line2) -> {
                    int extraFare = line.getExtraFare();
                    int extraFare2 = line2.getExtraFare();

                    return extraFare - extraFare2;
                }).orElseThrow(RuntimeException::new);

        return expensiveLine.getExtraFare();
    }

    public Fare getFare() {
        return this.fare;
    }

    public MemberResponse getMember() {
        return this.member;
    }
}
