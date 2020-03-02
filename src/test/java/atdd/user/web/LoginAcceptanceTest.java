package atdd.user.web;

import atdd.Constant;
import atdd.AbstractAcceptanceTest;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.domain.UserRepository;
import atdd.user.jwt.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;
    private LoginHttpTest loginHttpTest;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginAcceptanceTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @Test
    public void 로그인_요청하기() {
        //given
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
        LoginRequestView requestView = new LoginRequestView(EMAIL, PASSWORD, jwtTokenProvider);

        //when
        LoginResponseView response = loginHttpTest.login(requestView);

        //then
        assertThat(response.getAccessToken()).isNotEmpty();
        assertThat(response.getTokenType()).isNotEmpty();
    }
}