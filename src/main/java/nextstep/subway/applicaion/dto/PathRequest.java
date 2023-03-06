package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.ShortestPathType;

public class PathRequest {
    private Long source;
    private Long target;
    private ShortestPathType type;

    public PathRequest(Long source, Long target, ShortestPathType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public ShortestPathType getType() {
        return type;
    }
}
