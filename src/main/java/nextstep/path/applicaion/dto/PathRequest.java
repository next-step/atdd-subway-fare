package nextstep.path.applicaion.dto;

import nextstep.path.domain.PathType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull
    @Min(1)
    private final Long source;
    @NotNull
    @Min(1)
    private final Long target;
    @NotNull
    private final PathType type;

    public PathRequest(Long source, Long target, PathType type) {
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

    public PathType getType() {
        return type;
    }
}
