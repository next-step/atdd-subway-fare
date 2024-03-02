package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public long calculatePlusExtraFare(FareAgeGroup fareAgeGroup) {
        if (fareAgeGroup.isLineExtraFareFree()) {
            return 0;
        }
        return this.lines.stream()
                .mapToLong(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
