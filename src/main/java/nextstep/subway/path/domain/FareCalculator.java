package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.line.LineMaxFare;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.ArrayList;
import java.util.List;

public class FareCalculator implements FareCalculation {
    private FareParameter parameter;
    private List< BaseFarePolicy > baseFarePolicies = new ArrayList<>();
    private List< LineFarePolicy > lineFarePolicies = new ArrayList<>();
    private List< DiscountPolicy > discountPolicies = new ArrayList<>();
    private List< FareDistancePolicy > distanceFarePolicies = new ArrayList<>();

    public FareCalculator(FareParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Fare calculate() {
        final Fare distanceFare = Fare.sum(calculateBaseFare(), calculateDistanceFare());
        final Fare  totalFare= Fare.sum(distanceFare, calculateLineFare());
        return discountFare(totalFare);
    }

    public FareCalculator addDistanceFarePolicy(FareDistancePolicy distanceFarePolicy) {
        distanceFarePolicies.add(distanceFarePolicy);
        return this;
    }

    public FareCalculator addDiscountPolicy(DiscountPolicy discountPolicy) {
        discountPolicies.add(discountPolicy);
        return this;
    }

    public FareCalculator addBaseFarePolicy(BaseFarePolicy policy) {
        baseFarePolicies.add(policy);
        return this;
    }

    public FareCalculator addLineFarePolicy(LineMaxFare lineMaxFare) {
        lineFarePolicies.add(lineMaxFare);
        return this;
    }

    private Fare discountFare(Fare totalBaseFare) {
        return discountPolicies.stream()
                .map(policy -> policy.apply(totalBaseFare))
                .reduce(Fare.of(0), Fare::sum);
    }

    private Fare calculateDistanceFare() {
        return distanceFarePolicies.stream()
                .map(policy -> policy.calculate(parameter.getDistance()))
                .reduce(Fare.of(0), Fare::sum);
    }

    private Fare calculateBaseFare(){
        return baseFarePolicies.stream()
                .map(policy -> policy.calculate())
                .reduce(Fare.of(0), Fare::sum);
    }

    private Fare calculateLineFare(){
        return lineFarePolicies.stream()
                .map(policy -> policy.calculate(parameter.getLineMaxFare()))
                .reduce(Fare.of(0), Fare::sum);
    }

}
