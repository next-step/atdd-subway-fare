package nextstep.subway.applicaion;

import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.Path;

public interface FarePolicyHandler {

    int execute(int age, int requestFare, Path path);

    FarePolicyHandler chain(FarePolicy farePolicy);
}
