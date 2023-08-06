package subway.path.application.fare;

import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;

import java.util.List;

public class LineSurchargePathFare extends PathFareChain {
    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        List<Section> sections = calcInfo.getSections();
        long maxSurcharge = sections.stream()
                .mapToLong(section -> section.getLine().getSurcharge())
                .max()
                .orElse(0L) + calcInfo.getFare();

        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedFare(maxSurcharge);

        return super.nextCalculateFare(calcInfoResponse);
    }
}
