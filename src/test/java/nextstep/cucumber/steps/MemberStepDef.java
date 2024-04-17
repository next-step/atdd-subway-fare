package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.auth.AuthSteps;
import nextstep.cucumber.AcceptanceContext;
import nextstep.member.MemberSteps;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public MemberStepDef() {
        Given("어린이로 로그인 하고", () -> {
            String email = "email@email.com";
            String password = "password";
            MemberSteps.회원_생성_요청(email, password, 6);
            String token = AuthSteps.토큰_생성(email, password);
            context.store.put("accessToken", token);
        });
        Given("청소년으로 로그인 하고", () -> {
            String email = "email@email.com";
            String password = "password";
            MemberSteps.회원_생성_요청(email, password, 13);
            String token = AuthSteps.토큰_생성(email, password);
            context.store.put("accessToken", token);
        });
    }
}
