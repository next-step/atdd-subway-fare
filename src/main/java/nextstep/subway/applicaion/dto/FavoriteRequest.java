package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
