package nextstep.subway.path.domain.fare.line;

import nextstep.subway.line.domain.Lines;
import org.springframework.stereotype.Component;

@Component
public class BasicLineFarePolicy implements LineFarePolicy {
    @Override
    public int calculateAdditionalFare(Lines lines) {
        return lines.getAdditionalFares().stream()
                .mapToInt(fee -> fee)
                .max()
                .orElse(0);

    }
}
