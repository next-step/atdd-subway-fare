package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;
import org.springframework.lang.NonNull;

public class PathRequest {
    private Long source;
    private Long target;
    private PathType type;

    public PathRequest(@NonNull Long source, @NonNull Long target, PathType type) {
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
