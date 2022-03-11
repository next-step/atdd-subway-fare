package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

/**
 * 거리에 대한 요금
 */
public class DistanceFarePolicy implements FarePolicy {

    @Override
    public FareRequest fare(FareRequest fareRequest, Path path) {
        return new FareRequest(fareRequest.getAge(), path.distanceFare());
    }
}
