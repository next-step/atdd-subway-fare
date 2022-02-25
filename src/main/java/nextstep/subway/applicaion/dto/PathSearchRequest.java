package nextstep.subway.applicaion.dto;

public class PathSearchRequest {
    Long source;
    Long target;
    String type;

    public PathSearchRequest() {
    }

    public PathSearchRequest(Long source, Long target, String type) {
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

    public String getType() {
        return type;
    }
}
