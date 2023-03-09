package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy<Path> {
    private DistanceFareFormula distanceFareFormula;

    public DistanceFarePolicy(DistanceFareFormula distanceFareFormula) {
        this.distanceFareFormula = distanceFareFormula;
    }

    @Override
    public int apply(final Path path) {
        return distanceFareFormula.calculate(path.extractDistance());
    }
}
