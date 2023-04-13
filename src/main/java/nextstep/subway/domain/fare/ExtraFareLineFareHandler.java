package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Sections;

public class ExtraFareLineFareHandler extends FareHandler {
    private Sections sections;

    public ExtraFareLineFareHandler(FareHandler fareHandler, Sections sections) {
        super(fareHandler);
        this.sections = sections;
    }

    @Override
    public Fare handle(int distance) {
        Fare fare = Fare.of(sections.getHighestLineExtraFee())
                .add(super.handle(distance));
        return fare;
    }

}
