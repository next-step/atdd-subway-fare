package nextstep.subway.util.fare;

import nextstep.subway.domain.Line;

import java.util.List;

public class LineSurchargePolicy {
    public static int calculate(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max().orElse(0);
    }
}
