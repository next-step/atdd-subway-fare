package nextstep.subway.domain.service.fee;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.service.aggregation.StationPathAggregationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationFeeCalculateService {
    public static final BigDecimal baseFee = BigDecimal.valueOf(1250);

    private final AbstractStationPathFeeCalculator pathFeeCalculator;
    private final StationPathAggregationService stationPathAggregationService;
    private final StationLineRepository stationLineRepository;

    public BigDecimal calculateFee(BigDecimal distance, List<Long> pathStationIds) {
        final List<StationLine> totalLines = stationLineRepository.findAll();
        final List<StationLine> pathLines = stationPathAggregationService.getPathStationLine(totalLines, pathStationIds);

        return pathFeeCalculator.calculateFee(baseFee, StationPathFeeContext.builder()
                .distance(distance)
                .lines(pathLines)
                .build());
    }
}
