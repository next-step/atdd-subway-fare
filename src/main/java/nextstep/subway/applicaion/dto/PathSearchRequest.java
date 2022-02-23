package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathSearchType;
import org.springframework.web.bind.annotation.RequestParam;

public class PathSearchRequest {
    Long source;
    Long target;
    String method;

    public PathSearchRequest() {
    }

    public PathSearchRequest(Long source, Long target, String method) {
        this.source = source;
        this.target = target;
        this.method = method;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public String getMethod() {
        return method;
    }
}
