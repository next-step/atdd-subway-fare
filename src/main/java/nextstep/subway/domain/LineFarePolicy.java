package nextstep.subway.domain;

public class LineFarePolicy extends FarePolicy {

    private final Path path;

    public LineFarePolicy(Path path) {
        this.path = path;
    }

    @Override
    public boolean applicable() {
        return path.lineFareChargeable();
    }

    @Override
    public int calculate(int fare) {
        return fare + path.lineFare();
    }
}
