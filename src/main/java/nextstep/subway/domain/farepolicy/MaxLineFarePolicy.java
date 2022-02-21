package nextstep.subway.domain.farepolicy;

import java.util.function.Supplier;
import nextstep.subway.domain.Sections;

public class MaxLineFarePolicy implements Policy{

    private final Sections sections;

    public MaxLineFarePolicy(Sections sections) {
        this.sections = sections;
    }

    Supplier<Integer> expression;

    @Override
    public int calculate(int fare) {
        expression = () -> (maxLineFare(sections));

        return fare + expression.get();
    }

    private int maxLineFare(Sections sections) {
        return sections.getSections().stream()
            .mapToInt(value -> value.getLine().getAdditionalFare())
            .max().getAsInt();
    }
}
