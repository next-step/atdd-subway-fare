package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy<Integer> {
    private DistanceFareFormula distanceFareFormula;

    public DistanceFarePolicy(DistanceFareFormula distanceFareFormula) {
        this.distanceFareFormula = distanceFareFormula;
    }

    @Override
    public int apply(final Integer distance) {
        return distanceFareFormula.calculate(distance);
    }
}
