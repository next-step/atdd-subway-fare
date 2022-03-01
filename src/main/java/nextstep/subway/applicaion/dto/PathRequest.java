package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull @Positive
    private Long source;

    @NotNull @Positive
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

}
