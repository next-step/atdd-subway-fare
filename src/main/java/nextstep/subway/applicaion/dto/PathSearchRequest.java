package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathSearchType;
import org.springframework.web.bind.annotation.RequestParam;

public class PathSearchRequest {
    Long source;
    Long target;
    PathSearchType method;

    public PathSearchRequest() {
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathSearchType getMethod() {
        return method;
    }
}
