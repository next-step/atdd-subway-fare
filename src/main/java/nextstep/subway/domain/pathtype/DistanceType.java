package nextstep.subway.domain.pathtype;

import nextstep.subway.domain.Section;

public class DistanceType implements PathType {

    private static final String TYPE = "DISTANCE";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public double getWeight(Section section) {
        return section.getDistance();
    }
}
