package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
public class MemberStepDef implements En {
    @Autowired
    DatabaseCleanup databaseCleanup;
    ExtractableResponse<Response> response;

    String accessToken;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    public MemberStepDef() {
        Before(() -> {
            databaseCleanup.execute();
        });

        When("회원을 생성하면", () -> {
            response = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        });

        Then("회원이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("회원이 조회된다", () -> {
            response = MemberSteps.회원_정보_조회_요청(response);
            MemberSteps.회원_정보_조회됨(response, EMAIL, AGE);
        });

        Given("회원을 생성하고", () -> {
            response = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        });

        When("회원 정보를 수정하면", () -> {
            response = MemberSteps.회원_정보_수정_요청(response, "new" + EMAIL, "new" + PASSWORD, AGE);
        });

        Then("회원 정보가 수정된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });

        Given("로그인을 하고", () -> {
            accessToken = AuthSteps.로그인_요청(EMAIL, PASSWORD);
        });

        When("토큰을 통해 내 정보를 조회하면", () -> {
            response = MemberSteps.내_정보_조회_요청(accessToken);
        });

        Then("내 정보가 조회된다", () -> {
            MemberSteps.회원_정보_조회됨(response, EMAIL, AGE);
        });
    }
}
