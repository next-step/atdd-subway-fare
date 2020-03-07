package atdd.user.web;

import atdd.AbstractAcceptanceTest;
import atdd.TestConstant;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    public static final String USER_URL = "/users";

    public UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    void createUser() {
        // when
        UserResponseView response = userHttpTest.createUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_NAME, TestConstant.TEST_USER_PASSWORD).getResponseBody();

        // then
        assertThat(response.getName()).isEqualTo(TestConstant.TEST_USER_NAME);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser() {
        // given
        UserResponseView response = userHttpTest.createUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_PASSWORD, TestConstant.TEST_USER_NAME).getResponseBody();

        // when
        webTestClient.delete().uri(USER_URL + "/" + response.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName("회원 정보 조회")
    @Test
    void getUserInfo() {
        // given
        userHttpTest.createUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_NAME, TestConstant.TEST_USER_PASSWORD).getResponseBody();
        LoginResponseView responseView = userHttpTest
                .loginUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_PASSWORD).getResponseBody();

        webTestClient.get().uri(USER_URL + "/me")
                .header("Authorization",
                        String.format("%s %s", responseView.getTokenType(), responseView.getAccessToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.email").isEqualTo(TestConstant.TEST_USER_EMAIL);
    }
}