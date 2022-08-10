package nextstep.subway.domain;

public enum PathType {
    DISTANCE {
        public Path findPath(SubwayMap subwayMap, Station upStation, Station downStation) {
            return subwayMap.findPath(upStation, downStation);
        }
    },
    DURATION {
        public Path findPath(SubwayMap subwayMap, Station upStation, Station downStation) {
            return subwayMap.findPathByDuration(upStation, downStation);
        }
    };

    public abstract Path findPath(SubwayMap subwayMap, Station upStation, Station downStation);
}
