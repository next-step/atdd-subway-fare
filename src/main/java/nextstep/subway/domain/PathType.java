package nextstep.subway.domain;

public enum PathType {
    DISTANCE, DURATION;

    public int getWeight(Section section) {
        if (isDistance()) {
            return section.getDistance();
        }
        return section.getDuration();
    }

    private boolean isDistance() {
        return this == DISTANCE;
    }
}
