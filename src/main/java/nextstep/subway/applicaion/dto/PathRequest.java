package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.ShortestType;

public class PathRequest {

    private Long source;
    private Long target;

    private ShortestType type = ShortestType.DISTANCE;

    public PathRequest() {
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public ShortestType getType() {
        return type;
    }

    public void setType(ShortestType type) {
        this.type = type;
    }
}
