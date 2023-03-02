package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;

public class LineFarePolicyHandler extends FarePolicyHandler {
    @Override
    public Fare execute(Fare fare) {
        int additionalFare = fare.getPath()
                .includedLines()
                .stream()
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);

        return fare.withModified(fare.getCost() + additionalFare);
    }
}
