package atdd.user.web;

import atdd.AbstractAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void 회원_가입하기() {
        //when
        Long userId = userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //then
        assertEquals(1, userId);
    }

    @Test
    public void 회원_탈퇴하기() {
        //given
        Long userId = userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //when
        URI deletedURI = userHttpTest.deleteUser(userId);

        //then
        assertThat(deletedURI).isNull();
    }
}