package nextstep.subway.domain.pathtype;

import nextstep.subway.domain.Section;

public interface PathType {

    String getType();

    double getWeight(Section section);
}
