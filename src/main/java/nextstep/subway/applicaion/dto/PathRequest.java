package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public class PathRequest {
    private Long source;
    private Long target;
    private PathType type;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureDate;

    public PathRequest(@NonNull Long source, @NonNull Long target, PathType type, LocalDateTime departureDate) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.departureDate = departureDate;
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

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }
}
