package nextstep.core.subway.path.application;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class AdditionalFareCalculator {

    public int findMaxAdditionalFare(List<Integer> additionalFares) {
        return additionalFares.stream()
                .mapToInt(value -> value)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
