package nextstep.subway.domain.pathtype;

import nextstep.subway.domain.Section;

public class DurationType implements PathType {

    private static final String TYPE = "DURATION";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public double getWeight(Section section) {
        return section.getDuration();
    }
}
