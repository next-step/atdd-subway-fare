package nextstep.subway.application.dto;

import nextstep.subway.ui.controller.PathType;

public class FavoriteRequest {
    private Long source;
    private Long target;
    private PathType pathType;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target, PathType pathType) {
        this.source = source;
        this.target = target;
        this.pathType = pathType;
    }

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
