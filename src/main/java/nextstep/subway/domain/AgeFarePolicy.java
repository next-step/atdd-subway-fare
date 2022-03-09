package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

public class AgeFarePolicy implements FarePolicy {

    @Override
    public FareRequest fare(FareRequest fareRequest, Path path) {
        FareAgeEnum fareAgeEnum = FareAgeEnum.valueOf(fareRequest.getAge());
        return new FareRequest(fareRequest.getAge(),
                fareAgeEnum.getFareAge(fareRequest.getFare()));
    }
}
