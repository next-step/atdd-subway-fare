package nextstep.subway.path.dto;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;

public class PathParams {
    private Long source;
    private Long target;
    private PathType type;
    private LoginMember loginMember;

    public PathParams(Long source, Long target, PathType type, LoginMember loginMember) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.loginMember = loginMember;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathType getType() {
        return type;
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }
}
