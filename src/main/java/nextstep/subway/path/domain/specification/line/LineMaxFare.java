package nextstep.subway.path.domain.specification.line;

import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.valueobject.Fare;

public class LineMaxFare implements FarePolicy {

    private Fare lineFare;

    public LineMaxFare(Fare fare){
        this.lineFare = fare;
    }

    @Override
    public Fare calculate(Fare fare) {
        return Fare.sum(lineFare, fare);
    }
}
