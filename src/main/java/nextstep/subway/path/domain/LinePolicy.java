package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

public class LinePolicy implements FarePolicy{

    private Sections sections;

    private LinePolicy(Sections sections) {
        this.sections = sections;
    }

    public static LinePolicy from(Sections sections) {
        return new LinePolicy(sections);
    }

    @Override
    public Fare getFare(Fare fare) {
        fare.add(sections.getLineExtraChargeMax());
        return fare;
    }
}
