package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class FareLineStrategy implements FareCalculateStrategy {

    @Override
    public Fare calculate(Path path) {
        List<Line> lines = path.getLines();
        BigDecimal fare = getMostExpensiveLineFare(lines);

        return Fare.of(fare);
    }

    private BigDecimal getMostExpensiveLineFare(List<Line> lines) {
        return lines.stream()
                .map(Line::getFarePrimitive)
                .max(Comparator.naturalOrder())
                .orElseGet(() -> BigDecimal.ZERO);
    }

}
