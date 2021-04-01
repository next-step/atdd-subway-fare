package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

import java.util.List;
import java.util.NoSuchElementException;

public class FareByAddFare implements Fare {

    private final List<Section> sections;

    public FareByAddFare(Sections sections) {
        this.sections = sections.getSections();
    }

    @Override
    public int calculate() {
        return sections.stream()
                .mapToInt(it -> it.getLine().getAddFare())
                .max()
                .orElse(0);
    }
}
