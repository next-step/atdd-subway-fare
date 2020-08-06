package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.map.domain.PathType;

import java.time.LocalTime;

public class PathRequest {
    private Long source;
    private Long target;
    private PathType type;
    private LocalTime time;

    public PathRequest() {

    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public PathRequest(Long source, Long target, PathType type) {
        this(source, target);
        this.type = type;
    }

    public PathRequest(Long source, Long target, PathType type, LocalTime time) {
        this(source, target, type);
        this.time = time;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public PathType getType() {
        return type;
    }

    public void setType(PathType type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
