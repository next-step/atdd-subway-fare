package nextstep.path;

import nextstep.section.Section;

public class DistancePathFinder extends PathFinder {
    @Override
    int getWeight(Section section) {
        return section.getDistance();
    }
}
