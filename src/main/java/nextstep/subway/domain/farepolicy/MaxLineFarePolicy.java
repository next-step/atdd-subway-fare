package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Sections;

public class MaxLineFarePolicy implements Policy{

    private final Sections sections;

    public MaxLineFarePolicy(Sections sections) {
        this.sections = sections;
    }

    @Override
    public int calculate(int fare) {
        return fare + maxLineFare(sections);
    }

    private int maxLineFare(Sections sections) {
        return sections.getSections().stream()
            .mapToInt(value -> value.getLine().getAdditionalFare())
            .max().getAsInt();
    }
}
