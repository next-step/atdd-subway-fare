package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StationFeeCalculateService {
    public static final BigDecimal baseFee = BigDecimal.valueOf(1250);

    private final AbstractStationPathFeeCalculator firstSectionPathFeeCalculator;

    public BigDecimal calculateFee(BigDecimal distance) {
        return firstSectionPathFeeCalculator.calculateFee(baseFee, StationPathFeeContext.builder()
                .distance(distance)
                .build());
    }
}
