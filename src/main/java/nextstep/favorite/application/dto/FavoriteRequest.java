package nextstep.favorite.application.dto;

import nextstep.subway.constant.FindPathType;

public class FavoriteRequest {
    private Long source;
    private Long target;
    private FindPathType type;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public FindPathType getType() {
        return type;
    }
}
