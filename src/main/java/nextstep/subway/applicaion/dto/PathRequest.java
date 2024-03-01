package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import nextstep.subway.domain.PathSearchType;

public class PathRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;
    private PathSearchType type;

    public PathRequest() {

    }

    public PathRequest(Long source, Long target) {
        this(source, target, PathSearchType.DISTANCE);
    }

    public PathRequest(Long source, Long target, PathSearchType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public void setType(PathSearchType type) {
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathSearchType getType() {
        return type;
    }
}
