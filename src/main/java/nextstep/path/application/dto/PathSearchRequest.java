package nextstep.path.application.dto;

import nextstep.path.domain.PathType;
import nextstep.path.exception.PathSearchNotValidException;

import java.util.Objects;

public class PathSearchRequest {

    private Long source;
    private Long target;
    private PathType pathType;

    public PathSearchRequest() {
    }

    public PathSearchRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
        this.pathType = PathType.DISTANCE;
    }


    public PathSearchRequest(final Long source, final Long target, final PathType pathType) {
        this(source, target);
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

    public void setSource(final Long source) {
        this.source = source;
    }

    public void setTarget(final Long target) {
        this.target = target;
    }

    public void setPathType(final PathType pathType) {
        this.pathType = pathType;
    }

    public void validate() {
        if (Objects.isNull(source)) {
            throw new PathSearchNotValidException("source can not be null");
        }

        if (Objects.isNull(target)) {
            throw new PathSearchNotValidException("target can not be null");
        }

        if (Objects.isNull(pathType)) {
            throw new PathSearchNotValidException("pathType can not be null");
        }
    }
}
