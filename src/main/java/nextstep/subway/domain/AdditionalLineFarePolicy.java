package nextstep.subway.domain;

import java.util.List;

import static nextstep.subway.domain.Fare.ZERO_FARE;

public class AdditionalLineFarePolicy {
    public Fare calculateAdditionalLineFare(List<Line> lines) {
        return lines.stream()
                .map(Line::getAdditionalFare)
                .max(Fare::compareTo).orElse(ZERO_FARE);
    }
}
