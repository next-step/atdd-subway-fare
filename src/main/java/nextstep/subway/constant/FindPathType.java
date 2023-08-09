package nextstep.subway.constant;

import nextstep.subway.domain.EdgeWeight;
import nextstep.subway.domain.EdgeWeightDistance;
import nextstep.subway.domain.EdgeWeightDuration;

public enum FindPathType {
    DISTANCE("DISTANCE", "거리"),
    DURATION("DURATION", "시간");

    private final String type;
    private final String description;

    FindPathType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public static EdgeWeight getEdgeWeight(FindPathType type) {
        if (findPathType(type) == DISTANCE) {
            return new EdgeWeightDistance();
        }
        return new EdgeWeightDuration();
    }

    public static FindPathType findPathType(FindPathType type) {
        for (FindPathType findPathType : FindPathType.values()) {
            if (findPathType.equals(type)) {
                return findPathType;
            }
        }
        return null;
    }
}
