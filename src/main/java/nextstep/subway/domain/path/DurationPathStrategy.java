package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;

public class DurationPathStrategy implements PathStrategy {

    @Override
    public String getName() {
        return "DURATION";
    }

    @Override
    public int getType(Section section) {
        return section.getDuration();
    }
}
