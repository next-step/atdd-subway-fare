package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component("DURATION")
public class DurationPathStrategy implements PathStrategy {

    @Override
    public int getType(Section section) {
        return section.getDuration();
    }
}
