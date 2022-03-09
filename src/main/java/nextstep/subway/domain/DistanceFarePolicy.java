package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

public class DistanceFarePolicy implements FarePolicy {

    @Override
    public FareRequest fare(FareRequest fareRequest, Path path) {
        return new FareRequest(fareRequest.getAge(), path.distanceFare());
    }
}
