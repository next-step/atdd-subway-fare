package nextstep.member.application;

import java.util.List;

public class DecodedJwtToken {
    public final long userId;
    public final List<String> roles;

    public DecodedJwtToken(long userId, List<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }
}
