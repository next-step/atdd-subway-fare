package nextstep.subway.domain;

public enum PathType {
    DISTANCE {
        @Override
        public int getEdgeWeight(Section section) {
            return section.getDistance();
        }
    },
    DURATION {
        @Override
        public int getEdgeWeight(Section section) {
            return section.getDuration();
        }
    },
    ARRIVAL_TIME {
        @Override
        public int getEdgeWeight(Section section) {
            return 0;
        }
    };

    public abstract int getEdgeWeight(Section section);
}
