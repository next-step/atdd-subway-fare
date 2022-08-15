package nextstep.subway.payment;

import nextstep.subway.domain.Path;

@FunctionalInterface
public interface PathInformation {

    Path getPathResult();
}
