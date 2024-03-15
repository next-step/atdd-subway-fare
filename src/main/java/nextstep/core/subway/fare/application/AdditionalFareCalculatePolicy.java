package nextstep.core.subway.fare.application;

import nextstep.core.subway.path.application.FareCalculationContext;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AdditionalFareCalculatePolicy implements FareCalculatePolicy {

    @Override
    public int apply(FareCalculationContext context) {
        return context.getAdditionalFares().stream()
                .mapToInt(value -> value)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
