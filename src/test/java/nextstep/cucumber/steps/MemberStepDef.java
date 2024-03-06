package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.acceptance.AuthSteps;
import nextstep.cucumber.AcceptanceContext;
import nextstep.member.acceptance.MemberSteps;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public MemberStepDef() {
        Given("어린이로 로그인 하고", () -> {
            final String email = "kid@kid.com";
            final String password = "password";
            MemberSteps.회원_생성_요청(email, password, 10);
            final ExtractableResponse<Response> response = AuthSteps.로그인_요청(email, password);
            context.store.put("accessToken", response.jsonPath().getString("accessToken"));
        });
        Given("청소년으로 로그인 하고", () -> {
            final String email = "teen@teen.com";
            final String password = "password";
            MemberSteps.회원_생성_요청(email, password, 16);
            final ExtractableResponse<Response> response = AuthSteps.로그인_요청(email, password);
            context.store.put("accessToken", response.jsonPath().getString("accessToken"));
        });
    }
}
