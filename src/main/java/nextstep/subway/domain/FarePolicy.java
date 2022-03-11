package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FareRequest;

public interface FarePolicy {

    FareRequest fare(FareRequest fareRequest, Path path);
}
