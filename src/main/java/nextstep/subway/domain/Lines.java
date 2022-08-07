package nextstep.subway.domain;

import java.util.List;

public class Lines {
    private final List<Line> lines;
    private static final int MIN_SURCHARGE = 0;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getHigherSurCharge() {
        return lines.stream().map(Line::getSurcharge).max(Integer::compareTo).orElse(MIN_SURCHARGE);
    }
}
