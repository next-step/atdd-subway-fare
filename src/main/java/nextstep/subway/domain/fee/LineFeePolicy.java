package nextstep.subway.domain.fee;

import nextstep.subway.domain.Line;

import java.util.List;

public class LineFeePolicy extends FeePolicy {

    private final List<Line> lines;

    public LineFeePolicy(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    protected int calculateFee(int fee) {
        int surcharge = lines.stream().map(Line::getSurcharge).mapToInt(s -> s).max().orElse(0);
        return fee + surcharge;
    }
}
