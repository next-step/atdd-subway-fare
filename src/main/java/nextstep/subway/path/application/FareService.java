package nextstep.subway.path.application;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.FareCalculation;
import nextstep.subway.path.domain.FareCalculatorFactory;
import nextstep.subway.path.domain.FareParameter;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.valueobject.Fare;
import nextstep.subway.path.dto.FareRequest;
import nextstep.subway.path.dto.FareResponse;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    private FareCalculatorFactory fareCalculator;
    private PathService pathService;

    public FareService(PathService pathService) {
        this.pathService = pathService;
        this.fareCalculator = new FareCalculatorFactory();
    }

    public FareResponse calculate(FareRequest fareRequest, LoginMember loginMember) {
        final PathResult pathResult = pathService.findPath(fareRequest.getSource(), fareRequest.getTarget(), fareRequest.getType());
        final FareCalculation fareCalculator = FareCalculatorFactory.getFareCalculator(FareParameter.of(pathResult, loginMember));
        final Fare totalFare = fareCalculator.calculate();
        return FareResponse.of(pathResult, Fare.parseInt(totalFare));
    }
}
