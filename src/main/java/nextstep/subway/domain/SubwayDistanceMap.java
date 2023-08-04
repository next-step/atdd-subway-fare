package nextstep.subway.domain;

import java.util.List;

public class SubwayDistanceMap extends SubwayMap{
    public SubwayDistanceMap(List<Line> lines) {
        super(lines);
    }

    @Override
    int getWeight(Section section) {
        return section.getDistance();
    }
}
