package subway.path.application;

import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PathFareLineSurcharge extends PathFareChain{

    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        List<Section> sections = calcInfo.getSearchedSections();
        List<Long> surcharges = sections.stream()
                .map(section -> section.getLine().getSurcharge())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        long maxSurcharge = surcharges.get(0) + calcInfo.getFare();

        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedFare(maxSurcharge);
        return super.nextCalculateFare(calcInfoResponse);
    }
}
