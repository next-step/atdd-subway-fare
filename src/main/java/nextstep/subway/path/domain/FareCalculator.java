package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class FareCalculator {

    private final Sections sections;
    private final int age;

    public FareCalculator(Sections sections, int age) {
        this.sections = sections;
        this.age = age;
    }

    private final int BASIC_FARE = 1250;

    public int getTotalFare() {
        int distance = sections.getTotalDistance();
        return (int)((BASIC_FARE + calculateOverFare(distance) + getAdditionalFare() - getAgeDeduction()) * (1 - getAgeDiscount()));
    }

    private int calculateOverFare(int distance) {
        int over10KmFare = calculateOverDistanceFare(DistanceFarePolicy.TEN_KM, distance);
        int over50KmFare = calculateOverDistanceFare(DistanceFarePolicy.FIFTY_KM, distance);
        return over10KmFare + over50KmFare;
    }

    private int calculateOverDistanceFare(DistanceFarePolicy distanceFarePolicy, int distance){
        distance -= distanceFarePolicy.getOverChargeDistance();
        if(distance>0){
            int calculatedFare = (int) ((Math.ceil((distance - 1) / distanceFarePolicy.doChargeEveryNKm()) + 1) * 100);
            return Math.min(distanceFarePolicy.getMaximumFare(), calculatedFare);
        }
        return 0;
    }

    private int getAdditionalFare(){
        Set<Line> goThroughLine = sections.getSections()
                .stream()
                .map(section -> section.getLine())
                .collect(Collectors.toSet());

        int maxFare = goThroughLine
                .stream()
                .mapToInt(line -> LineFarePolicy.find(line.getId()))
                .max()
                .orElseThrow(NoSuchElementException::new);

        return maxFare;
    }

    private AgeFarePolicy matchingAgeFarePolicy(){
        return AgeFarePolicy.getMatchingAgeFarePolicy(age);
    }

    private double getAgeDiscount(){
        return matchingAgeFarePolicy().getDiscount();
    }

    private int getAgeDeduction(){
        return matchingAgeFarePolicy().getDeduction();
    }


}
