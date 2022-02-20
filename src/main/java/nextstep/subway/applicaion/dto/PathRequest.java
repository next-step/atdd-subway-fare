package nextstep.subway.applicaion.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

public class PathRequest {
    private long source;
    private long target;
    private LocalDateTime time;

    @JsonCreator
    public PathRequest(long source, long target, @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime time) {
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
