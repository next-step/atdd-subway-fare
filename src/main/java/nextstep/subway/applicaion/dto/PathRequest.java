package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;

import java.util.StringJoiner;

public class PathRequest {

    private Long source;
    private Long target;
    private PathType type;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", PathRequest.class.getSimpleName() + "[", "]")
                .add("source=" + source)
                .add("target=" + target)
                .add("type=" + type)
                .toString();
    }

}
