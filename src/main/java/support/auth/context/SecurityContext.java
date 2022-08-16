package support.auth.context;

import java.io.Serializable;

public class SecurityContext implements Serializable {
    private Authentication authentication = Authentication.NULL;

    public SecurityContext() {
    }

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}