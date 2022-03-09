package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FareRequest;
import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.Path;

public interface FarePolicyHandler {

    int execute(FareRequest fareRequest, Path path);

    FarePolicyHandler chain(FarePolicy farePolicy);
}
