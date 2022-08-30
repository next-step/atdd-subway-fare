package nextstep.subway.domain;

import nextstep.subway.domain.SubwayMap.ArrivalTimeSubwayMap;
import nextstep.subway.domain.SubwayMap.DistanceSubwayMap;
import nextstep.subway.domain.SubwayMap.DurationSubwayMap;
import nextstep.subway.domain.SubwayMap.SubwayMap;

import java.util.List;

public enum PathType {
    DISTANCE {
        @Override
        public SubwayMap getInstance(List<Line> lines) {
            return new DistanceSubwayMap(lines);
        }
    },
    DURATION {
        @Override
        public SubwayMap getInstance(List<Line> lines) {
            return new DurationSubwayMap(lines);
        }
    },
    ARRIVAL_TIME {
        @Override
        public SubwayMap getInstance(List<Line> lines) {
            return new ArrivalTimeSubwayMap(lines);
        }
    };

    public abstract SubwayMap getInstance(List<Line> lines);
}
