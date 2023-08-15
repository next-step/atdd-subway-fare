package nextstep.domain.subway.Fare;

import nextstep.domain.subway.Line;

import java.util.List;

public class LineFarePolicy extends FarePolicy{
    private List<Line> lines;

    public LineFarePolicy(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public int calculateFare(int fare) {
        int surcharge = lines.stream().map(Line::getSurCharge).mapToInt(s -> s).max().orElse(0);

        return fare + surcharge;
    }
}
