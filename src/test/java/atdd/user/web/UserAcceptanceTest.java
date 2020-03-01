package atdd.user.web;

import atdd.AbstractAcceptanceTest;
import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import atdd.user.domain.UserRepository;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private User user = new User(1L, EMAIL, NAME, PASSWORD);
    private UserHttpTest userHttpTest;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserAcceptanceTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void 회원_가입하기() {
        //given
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        given(userService.createUser(any(CreateUserRequestView.class)))
                .willReturn(UserResponseView.of(user));

        //when
        Long userId = userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //then
        assertEquals(1, userId);
    }

    @Test
    public void 회원_탈퇴하기() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userService.createUser(any(CreateUserRequestView.class)))
                .willReturn(UserResponseView.of(user));
        Long userId = userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //when
        URI deletedURI = userHttpTest.deleteUser(userId);

        //then
        assertThat(deletedURI).isNull();
    }

    @Test
    public void 회원정보_요청하기() {
        //given
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
        String token = jwtTokenProvider.createToken(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(user);

        //when
        UserResponseView responseView = userHttpTest.retrieveUserInfo(token);

        //then
        assertThat(responseView.getEmail()).isEqualTo(EMAIL);
        assertThat(responseView.getName()).isEqualTo(NAME);
        assertThat(responseView.getPassword()).isEqualTo(PASSWORD);
    }
}