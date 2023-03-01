package nextstep.subway.domain;

public class DistancePathType implements PathType {

    @Override
    public double getWeight(Section section) {
        return section.getDistance().getValue();
    }
}
