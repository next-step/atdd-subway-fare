package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;

public interface PathStrategy {

    String getName();
    int getType(Section section);
}
