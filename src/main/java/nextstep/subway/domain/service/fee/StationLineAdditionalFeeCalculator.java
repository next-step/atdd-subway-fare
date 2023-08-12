package nextstep.subway.domain.service.fee;

import lombok.Builder;
import nextstep.subway.domain.StationLine;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class StationLineAdditionalFeeCalculator extends AbstractStationPathFeeCalculator {
    @Builder
    public StationLineAdditionalFeeCalculator() {
    }

    @Override
    public BigDecimal calculate(StationPathFeeContext context) {
        return getMaximumFeeOfLineAdditionalFee(context.getLines());
    }

    private BigDecimal getMaximumFeeOfLineAdditionalFee(List<StationLine> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            return BigDecimal.ZERO;
        }

        return lines.stream()
                .map(StationLine::getAdditionalFee)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
