package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component("DISTANCE")
public class DistancePathStrategy implements PathStrategy {

    @Override
    public int getType(Section section) {
        return section.getDistance();
    }


}
