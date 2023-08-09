package nextstep.subway.domain.farechain;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;

public class LineOverFare extends OverFarePolicyHandler {

    public LineOverFare(OverFarePolicyHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public int chargeOverFare(Path path) {

        return super.chargeHandler(path, getMostExpensiveLineUsageFee(path));
    }

    private static int getMostExpensiveLineUsageFee(Path path) {
        return path.getLines().stream().mapToInt(Line::getUsageFee).max().orElse(0);
    }
}
