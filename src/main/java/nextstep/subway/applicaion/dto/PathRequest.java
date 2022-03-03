package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive
    private Long source;
    @Positive
    private Long target;
    @NotNull
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

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public void setPathType(PathType pathType) {
        this.pathType = pathType;
    }
}
