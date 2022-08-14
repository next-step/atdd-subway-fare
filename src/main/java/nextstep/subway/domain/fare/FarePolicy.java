package nextstep.subway.domain.fare;

import nextstep.subway.domain.Sections;

import javax.naming.OperationNotSupportedException;

public interface FarePolicy {
    long calculateOverFare(Sections sections);
}
