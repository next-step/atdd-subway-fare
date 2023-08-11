package nextstep.favorite.application.dto;

import nextstep.subway.domain.PathFindType;

public class FavoriteRequest {
    private Long source;
    private Long target;

    // 사용자들은 보통 빨리가고 싶어할 것이기 때문에 default PathFindType이 DURATION
    private PathFindType pathFindType = PathFindType.DURATION;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target, PathFindType pathFindType) {
        this.source = source;
        this.target = target;
        this.pathFindType = pathFindType;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathFindType getPathFindType() {
        return pathFindType;
    }
}
