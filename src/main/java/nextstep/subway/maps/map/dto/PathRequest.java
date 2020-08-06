package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.map.domain.PathType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PathRequest {

    public final static String DATE_FORMAT_PATTERN = "yyyyMMddHHmm";
    private Long source;
    private Long target;
    private PathType type;
    private String time;

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

    public PathRequest(Long source, Long target, PathType type, String time) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalTime getLocalTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        return LocalTime.parse(this.time, dateTimeFormatter);
    }
}
