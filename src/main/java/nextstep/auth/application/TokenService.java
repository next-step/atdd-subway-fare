package nextstep.auth.application;

import nextstep.auth.application.dto.TokenResponse;
import nextstep.global.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        final UserDetails userDetails = userDetailService.findByEmail(email);

        if (userDetails.isAnonymous()) {
            throw new IllegalArgumentException("등록된 회원이 아닙니다.");
        }

        if (!userDetails.isSamePassword(password)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(email);

        return new TokenResponse(token);
    }
}
