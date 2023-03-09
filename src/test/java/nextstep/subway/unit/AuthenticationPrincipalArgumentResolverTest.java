package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.AnonymousUser;
import nextstep.member.domain.AuthenticatedUser;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.domain.SearchType;
import nextstep.subway.ui.PathController;
import nextstep.subway.utils.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthenticationPrincipalArgumentResolverTest {

    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MethodParameter parameter;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Mock
    private NativeWebRequest webRequest;

    private String tokenOf성인사용자;
    private String tokenOf청소년사용자;
    private String tokenOf어린이사용자;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // authenticationPrincipalArgumentResolver 객체 할당
        authenticationPrincipalArgumentResolver = new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, memberService);

        // resolveArgument 메서드의 MethodParameter 할당 (AuthenticationPrincipal 어노테이션이 붙은 첫번째 인자만 테스트한다.)
        parameter = new MethodParameter(PathController.class.getDeclaredMethod("findPath", AuthenticatedUser.class, Long.class, Long.class, SearchType.class), 0);

        // 사용자 authentication
        Member 성인사용자 = memberRepository.findByEmail(Users.성인.getEmail()).get();
        Member 청소년사용자 = memberRepository.findByEmail(Users.청소년.getEmail()).get();
        Member 어린이사용자 = memberRepository.findByEmail(Users.어린이.getEmail()).get();

        tokenOf성인사용자 = jwtTokenProvider.createToken(성인사용자.getId().toString(), Arrays.asList(RoleType.ROLE_MEMBER.name()));
        tokenOf청소년사용자 = jwtTokenProvider.createToken(청소년사용자.getId().toString(), Arrays.asList(RoleType.ROLE_MEMBER.name()));
        tokenOf어린이사용자 = jwtTokenProvider.createToken(어린이사용자.getId().toString(), Arrays.asList(RoleType.ROLE_MEMBER.name()));
    }

    @Test
    void 성인사용자() throws Exception {
        when(webRequest.getHeader("Authorization")).thenReturn(createAuthorizationHeader(tokenOf성인사용자));

        LoginMember 성인 = (LoginMember) authenticationPrincipalArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(tokenOf성인사용자));
        Member member = memberService.getMember(id);
        LoginMember loginMember = new LoginMember(member.getId(), member.getAge(), member.getRoles());
        assertThat(성인).isEqualTo(loginMember);
    }

    private String createAuthorizationHeader(String token) {
        return String.format("bearer %s", token);
    }

    @Test
    void 청소년사용자() throws Exception {
        when(webRequest.getHeader("Authorization")).thenReturn(createAuthorizationHeader(tokenOf청소년사용자));

        LoginMember 청소년 = (LoginMember) authenticationPrincipalArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(tokenOf청소년사용자));
        Member member = memberService.getMember(id);
        LoginMember loginMember = new LoginMember(member.getId(), member.getAge(), member.getRoles());
        assertThat(청소년).isEqualTo(loginMember);
    }

    @Test
    void 어린이사용자() throws Exception {
        when(webRequest.getHeader("Authorization")).thenReturn(createAuthorizationHeader(tokenOf어린이사용자));

        LoginMember 어린이 = (LoginMember) authenticationPrincipalArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(tokenOf어린이사용자));
        Member member = memberService.getMember(id);
        LoginMember loginMember = new LoginMember(member.getId(), member.getAge(), member.getRoles());
        assertThat(어린이).isEqualTo(loginMember);
    }

    @Test
    void 익명사용자() throws Exception {
        when(webRequest.getHeader("Authorization")).thenReturn(null);

        AnonymousUser 익명사용자 = (AnonymousUser) authenticationPrincipalArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        assertThat(익명사용자).isInstanceOf(AnonymousUser.class);
    }
}
