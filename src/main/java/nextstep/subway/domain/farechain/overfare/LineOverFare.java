package nextstep.subway.domain.farechain.overfare;

import java.util.List;
import nextstep.subway.domain.Line;

public class LineOverFare extends OverFarePolicyHandler {

    private final List<Line> lines;

    public LineOverFare(List<Line> lines) {
        super(null);
        this.lines = lines;
    }

    @Override
    public int chargeOverFare(int fare) {

        return super.chargeHandler(fare + getMostExpensiveLineUsageFee());
    }

    private int getMostExpensiveLineUsageFee() {
        return lines.stream().mapToInt(Line::getUsageFee).max().orElse(0);
    }
}
