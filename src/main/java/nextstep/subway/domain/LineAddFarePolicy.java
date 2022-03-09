package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

/**
 * 호선 추가 요금
 */
public class LineAddFarePolicy implements FarePolicy {

    @Override
    public FareRequest fare(FareRequest fareRequest, Path path) {
        return new FareRequest(fareRequest.getAge(), path.maxLineAdditionFare() + fareRequest.getFare());
    }
}
