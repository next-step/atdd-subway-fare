package nextstep.subway.domain;

import java.util.List;

public class SubwayDurationMap extends SubwayMap {
    public SubwayDurationMap(List<Line> lines) {
        super(lines);
    }

    @Override
    int getWeight(Section section) {
        return section.getDuration();
    }
}
