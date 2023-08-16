package nextstep.api.auth.aop.principal;

import lombok.Getter;

@Getter
public class AnonymousPrincipal extends UserPrincipal {

    public AnonymousPrincipal() {
        super("", "");
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
