package nextstep.subway.domain;

public class DurationPathType implements PathType {

    @Override
    public double getWeight(Section section) {
        return section.getDuration().getValue();
    }
}
