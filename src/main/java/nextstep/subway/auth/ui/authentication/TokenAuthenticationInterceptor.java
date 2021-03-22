package nextstep.subway.auth.ui.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.handler.IssueTokenSuccessHandler;
import nextstep.subway.auth.application.handler.SimpleUrlAuthenticationFailureHandler;
import nextstep.subway.auth.application.provider.AuthenticationManager;
import nextstep.subway.auth.application.provider.AuthenticationProvider;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private ObjectMapper objectMapper;
    private AuthenticationManager authenticationManager;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(new IssueTokenSuccessHandler(jwtTokenProvider), new SimpleUrlAuthenticationFailureHandler());
        this.authenticationManager = new AuthenticationProvider(userDetailsService);
        this.objectMapper = objectMapper;
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        return authenticationManager.authenticate(authenticationToken);
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }
}
