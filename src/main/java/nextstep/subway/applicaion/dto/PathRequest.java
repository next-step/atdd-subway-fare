package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.PathSearchType;

@Getter
public class PathRequest {

    private Long source;
    private Long target;

    private PathSearchType type;

    public PathRequest(Long source, Long target, PathSearchType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }
}
