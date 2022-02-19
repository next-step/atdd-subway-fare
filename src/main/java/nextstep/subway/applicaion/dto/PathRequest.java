package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;

public class PathRequest {

    private Long source;

    private Long target;

    private PathType pathType;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathType getPathType() {
        return pathType;
    }

}
