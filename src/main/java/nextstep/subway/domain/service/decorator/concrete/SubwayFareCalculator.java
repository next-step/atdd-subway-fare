package nextstep.subway.domain.service.decorator.concrete;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;
import org.springframework.stereotype.Component;

@Component
public class SubwayFareCalculator {

    public int execute(Member member, Path path) {
        return getFareCalculator(member, path).appendFare();
    }

    private SurchargeCalculator getFareCalculator(Member member, Path path) {
        return new AgeSurchargeDecorator(getLineSurchargeDecorator(path), member);
    }

    private LineSurchargeDecorator getLineSurchargeDecorator(Path path) {
        return new LineSurchargeDecorator(getDistanceCalculator(path.extractDistance()), path.findIncludedLines());
    }

    private SurchargeCalculator getDistanceCalculator(int distance) {
        return new DistanceSurchargeCalculate(distance);
    }


}
