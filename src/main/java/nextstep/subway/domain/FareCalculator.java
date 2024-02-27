package nextstep.subway.domain;

import org.springframework.stereotype.Service;

public interface FareCalculator {
    int calculateFare(int distance);
}
