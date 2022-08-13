package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component
public class DistancePathStrategy implements PathStrategy {

    @Override
    public String getName() {
        return "DISTANCE";
    }

    @Override
    public int getType(Section section) {
        return section.getDistance();
    }


}
