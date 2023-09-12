package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.utils.path.PathFareCalculator;

@Getter
public class Path {

    private Sections sections;
    private Integer fare;

    public Path(Sections sections) {
        this.sections = sections;
        this.fare = PathFareCalculator.calculate(sections.getTotalDistance());
    }

}
