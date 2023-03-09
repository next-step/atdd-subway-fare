package nextstep.subway.domain;

public class LineAdditionalFarePolicy implements FarePolicy<Path> {

    @Override
    public int apply(final Path path) {
        return path.getSections().extractHighestAdditionalFare();
    }
}
