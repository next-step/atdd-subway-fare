package nextstep.subway.path.application.dto;

public class PathRequest {
    private Long source;
    private Long target;
    private String type;

    public PathRequest(Long source,
                       Long target,
                       String type) {
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
