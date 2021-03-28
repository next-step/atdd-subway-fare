package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.AdditionalFareCalculator;
import nextstep.subway.path.domain.DiscountFareCalculator;
import nextstep.subway.path.domain.PathResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FareDiscountService {
    private final LineService lineService;

    public FareDiscountService(LineService lineService) {
        this.lineService = lineService;
    }

    private List<Line> findLinesOfPath(PathResult pathResult) {
        List<Line> allLine = lineService.findLines();
        return pathResult.filterLineHasSection(allLine);
    }

    public int calculateDiscount(LoginMember loginMember, int fare) {
        DiscountFareCalculator calculator = new DiscountFareCalculator(loginMember);
        return calculator.calculate(fare);
    }

    public int calculateAdditionalFare(PathResult pathResult) {
        AdditionalFareCalculator additionalFareCalculator = new AdditionalFareCalculator(
            findLinesOfPath(pathResult)
        );

        return additionalFareCalculator.calculate(pathResult.getTotalDistance());
    }
}
