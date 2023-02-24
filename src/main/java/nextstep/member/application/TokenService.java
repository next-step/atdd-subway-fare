package nextstep.member.application;

import nextstep.common.exception.MissingTokenException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.common.error.MemberError.NOT_MISSING_TOKEN;
import static nextstep.common.error.MemberError.NOT_VALID_TOKEN;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider, final GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(final String email, final String password) {
        final Member member = memberService.login(email, password);
        final String token = createToken(member.getEmail(), member.getRoles());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(final String code) {
        final String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        final GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);
        final Member member = memberService.createOrFindMember(githubProfile.getEmail());
        final String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(final String credentials) {
        if (credentials.isEmpty()) {
            throw new MissingTokenException(NOT_MISSING_TOKEN);
        }
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new MissingTokenException(NOT_VALID_TOKEN);
        }
        final String email = jwtTokenProvider.getPrincipal(credentials);
        final Member member = memberService.findByEmail(email);
        return LoginMember.from(member);
    }

    private String createToken(final String email, final List<String> roles) {
        return jwtTokenProvider.createToken(email, roles);
    }
}
