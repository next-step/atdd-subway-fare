package nextstep.subway.domain.service;

import nextstep.subway.domain.StationFeePolicyBySection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class StationFeeCalculateService {
    public static final BigDecimal baseFee = BigDecimal.valueOf(1250);

    public BigDecimal calculateFee(BigDecimal distance) {
        final BigDecimal additionalFee = Arrays.stream(StationFeePolicyBySection.values())
                .map(policy -> policy.calculateSectionFee(distance))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return baseFee.add(additionalFee);
    }
}
