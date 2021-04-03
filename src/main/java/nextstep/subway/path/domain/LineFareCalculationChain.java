package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class LineFareCalculationChain implements FareCalculationChain {

    private FareCalculationChain chain;

    @Override
    public void setNextChain(FareCalculationChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public Fare calculate(FareCalculationCriteria criteria, Fare fare) {
        List<Line> lines = criteria.getLines();
        int addedFare = lines.stream()
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);
        fare = new Fare(fare.getFare() + addedFare);

        return this.chain.calculate(criteria, fare);
    }
}
