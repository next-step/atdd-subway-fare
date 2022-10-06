package nextstep.subway.applicaion.dto;

import nextstep.subway.util.pathfinder.PathType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class PathRequest {
    private Long source;

    private Long target;

    private PathType type;

    @DateTimeFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime time;

    public PathRequest(Long source, Long target, PathType type, LocalDateTime time) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.time = time;
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

    public LocalDateTime getTime() {
        return time;
    }
}
