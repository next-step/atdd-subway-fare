package nextstep.auth.authentication;

import nextstep.auth.authentication.handler.SaveSessionSuccessHandler;
import nextstep.auth.authentication.handler.SimpleUrlAuthenticationFailureHandler;
import nextstep.auth.authentication.provider.AuthenticationManager;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import nextstep.auth.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SessionAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private AuthenticationManager authenticationManager;

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService) {
        super(new SaveSessionSuccessHandler(), new SimpleUrlAuthenticationFailureHandler());
        this.authenticationManager = new AuthenticationProvider(userDetailsService);
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationToken authenticationToken = convert(request);
        return authenticationManager.authenticate(authenticationToken);
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
