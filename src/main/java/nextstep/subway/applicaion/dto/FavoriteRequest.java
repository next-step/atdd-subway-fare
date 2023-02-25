package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {

    @NotNull(message = "필수값이 존재하지 않습니다.")
    private Long source;
    @NotNull(message = "필수값이 존재하지 않습니다.")
    private Long target;

    private FavoriteRequest() {}

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
