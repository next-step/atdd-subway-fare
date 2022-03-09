package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

/**
 * 나이별 추가 요금
 */
public class AgeFarePolicy implements FarePolicy {

    @Override
    public FareRequest fare(FareRequest fareRequest, Path path) {
        FareAgeEnum fareAgeEnum = FareAgeEnum.valueOf(fareRequest.getAge());
        return new FareRequest(fareRequest.getAge(),
                fareAgeEnum.getFareAge(fareRequest.getFare()));
    }
}
