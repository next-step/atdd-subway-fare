package nextstep.subway.domain.policy.fare;

import nextstep.subway.domain.Line;
import org.springframework.stereotype.Component;

@Component
public class ExtraLineFare implements FarePolicy {

    @Override
    public boolean supports(PathByFare pathByFare) {
        return true;
    }

    @Override
    public int fare(PathByFare pathByFare) {
        return pathByFare.lines()
                .stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
