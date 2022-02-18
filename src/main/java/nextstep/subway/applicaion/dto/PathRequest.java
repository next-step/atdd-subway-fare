package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class PathRequest {
    private long source;
    private long target;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime time;

    public PathRequest(long source, long target, LocalDateTime time) {
        this.source = source;
        this.target = target;
        this.time = time;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
