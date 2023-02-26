package nextstep.subway.domain;

public enum PathRequestType {
    DISTANCE, DURATION;

    public int getWeight(Section section) {
        if (this == DURATION) {
            return section.getDuration();
        }
        return section.getDistance();
    }
}
