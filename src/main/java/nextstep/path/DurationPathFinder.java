package nextstep.path;

import nextstep.section.Section;

public class DurationPathFinder extends PathFinder {
    @Override
    int getWeight(Section section) {
        return section.getDuration();
    }
}
