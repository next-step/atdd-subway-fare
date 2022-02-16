package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;

public enum LineFarePolicy {
    일호선("일호선", 500),
    이호선("이호선", 100),
    삼호선("삼호선", 0),
    사호선("사호선", 300),
    분당선("분당선", 400),
    신분당선("신분당선", 1_000);

    public final String lineName;
    public final int extraCharge;

    LineFarePolicy(final String lineName, final int extraCharge) {
        this.lineName = lineName;
        this.extraCharge = extraCharge;
    }

    public static int calculate(List<String> lineNames) {
        return Arrays.stream(LineFarePolicy.values())
                .filter(it -> lineNames.contains(it.lineName))
                .mapToInt(it -> it.extraCharge)
                .max()
                .orElse(0);
    }
}
