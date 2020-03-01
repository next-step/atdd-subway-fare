package atdd.login.docs;

import atdd.AbstractDocumentationTest;
import atdd.login.web.LoginController;
import atdd.member.application.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(LoginController.class)
public class LoginDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private MemberService memberService;

    @Test
    void beAbleToLogin() throws Exception {

    }

}