package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;

public class PathRequest {
    Long source;
    Long target;
    PathType type;

    public PathRequest(Long source, Long target, String type) {
        this.source = source;
        this.target = target;
        this.type = PathType.convertType(type);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathType getType() {
        return type;
    }
}
