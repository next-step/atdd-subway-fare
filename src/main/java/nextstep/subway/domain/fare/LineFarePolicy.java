package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class LineFarePolicy {

    public int getFare(final Path path) {
        return path.getSections()
                .getSections()
                .stream()
                .mapToInt(section -> section.getLine().getFare())
                .max()
                .orElse(0);
    }
}
