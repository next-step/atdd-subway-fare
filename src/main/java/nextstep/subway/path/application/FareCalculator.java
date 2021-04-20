package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.enums.AgeFarePolicy;
import nextstep.subway.path.domain.enums.DistanceFarePolicy;
import nextstep.subway.path.domain.enums.LineFarePolicy;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class FareCalculator {

    public FareCalculator() {
    }

    public int getTotalFare(Set<Line> goThroughLine, int distance, int age) {
        Fare fare = new Fare();
        fare = calculateFareByDistanceFarePolicy(fare,distance);
        fare = calculateFareByLineFarePolicy(fare,goThroughLine);
        fare = calculateFareByAgeFarePolicy(fare,age);

        return fare.getFare();
    }

    private Fare calculateFareByDistanceFarePolicy(Fare fare, int distance) {
        if(distance<=10){
            return fare;
        }
        Fare distanceFare = new Fare(fare.getFare()+calculateOverFare(distance));
        return distanceFare;
    }

    private Fare calculateFareByLineFarePolicy(Fare fare, Set<Line> goThroughLine) {
        Fare lineFare = new Fare(fare.getFare()+getAdditionalFare(goThroughLine));
        return lineFare;
    }

    private Fare calculateFareByAgeFarePolicy(Fare fare, int age) {
        AgeFarePolicy ageFarePolicy = matchingAgeFarePolicy(age);
        Fare ageFare = new Fare((int)((fare.getFare() - getAgeDeduction(ageFarePolicy)) * (1 - getAgeDiscount(ageFarePolicy))));
        return ageFare;
    }

    private int calculateOverFare(int distance) {
        int over10KmFare = calculateOverDistanceFare(DistanceFarePolicy.TEN_KM, distance);
        int over50KmFare = calculateOverDistanceFare(DistanceFarePolicy.FIFTY_KM, distance);
        return over10KmFare + over50KmFare;
    }

    private int calculateOverDistanceFare(DistanceFarePolicy distanceFarePolicy, int distance) {
        distance -= distanceFarePolicy.getDistanceFrom();
        if (distance > 0) {
            int calculatedFare = (int) ((Math.ceil((distance - 1) / distanceFarePolicy.getChargeEveryNKm()) + 1) * 100);
            return Math.min(distanceFarePolicy.getMaximumFare(), calculatedFare);
        }
        return 0;
    }

    private int getAdditionalFare(Set<Line> goThroughLine) {
        int maxFare = goThroughLine
                .stream()
                .mapToInt(line -> LineFarePolicy.find(line.getId()))
                .max()
                .orElseThrow(NoSuchElementException::new);

        return maxFare;
    }

    private AgeFarePolicy matchingAgeFarePolicy(int age) {
        return AgeFarePolicy.getMatchingAgeFarePolicy(age);
    }

    public double getAgeDiscount(AgeFarePolicy ageFarePolicy) {
        return ageFarePolicy.getDiscount();
    }

    public int getAgeDeduction(AgeFarePolicy ageFarePolicy) {
        return ageFarePolicy.getDeduction();
    }

}
