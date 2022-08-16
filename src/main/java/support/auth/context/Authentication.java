package support.auth.context;

import support.auth.authentication.AuthenticationException;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

import java.io.Serializable;
import java.util.List;

public class Authentication implements Serializable {
    private Object principal;
    private List<String> authorities;

    public Authentication(Object principal, List<String> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    public Authentication() {

    }

    public Object getPrincipal() {
        return principal;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public User toUser(AuthenticationPrincipal authenticationPrincipal) {
        return new User(getPrincipal().toString(), null, getAuthorities());
    }

    public static final Authentication NULL = new Authentication() {
        public User toUser(AuthenticationPrincipal authenticationPrincipal) {
            if(!authenticationPrincipal.required()) {
                return new User();
            }
            throw new AuthenticationException();
        }
    };


}
