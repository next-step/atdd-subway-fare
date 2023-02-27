package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.login(email, password);

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);

        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        Member member = memberService.createOrFindMember(githubProfile.getEmail());

        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRoles());

        return new TokenResponse(token);
    }
}
