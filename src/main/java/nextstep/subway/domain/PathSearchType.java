package nextstep.subway.domain;

public enum PathSearchType {
    DURATION, DISTANCE;

    public int getWeight(Section section) {
        if(this == DURATION) {
            return section.getDuration();
        }

        if (this == DISTANCE ) {
            return section.getDistance();
        }

        throw new IllegalArgumentException("지원하지 않는 타입입니다.");
    }
}
