package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;

public interface PathStrategy {

    int getType(Section section);
}
