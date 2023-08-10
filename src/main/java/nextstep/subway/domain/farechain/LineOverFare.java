package nextstep.subway.domain.farechain;

import java.util.List;
import nextstep.subway.domain.Line;

public class LineOverFare extends OverFarePolicyHandler {

    private final List<Line> lines;

    public LineOverFare(OverFarePolicyHandler nextHandler, List<Line> lines) {
        super(nextHandler);
        this.lines = lines;
    }

    @Override
    public int chargeOverFare() {

        return super.chargeHandler(getMostExpensiveLineUsageFee());
    }

    private int getMostExpensiveLineUsageFee() {
        return lines.stream().mapToInt(Line::getUsageFee).max().orElse(0);
    }
}
