package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.Optional;

public class FareParameter {
    private Distance distance;
    private Fare lineMaxFare;
    private LoginMember member;

    private FareParameter(Distance distance, Fare lineMaxFare, LoginMember member){
        this.distance = distance;
        this.lineMaxFare = lineMaxFare;
        this.member = member;
    }

    public static FareParameter of(PathResult pathResult, LoginMember member){
        final Distance distance = Distance.of(pathResult.getTotalDistance());
        final Fare fare = Fare.of(pathResult.getLineMaxFare());
        return new FareParameter(distance, fare, member);
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getLineMaxFare() {
        return lineMaxFare;
    }

    public Age getAge(){
        return Age.of(member.getAge());
    }
}
