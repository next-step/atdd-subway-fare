package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.auth.utils.steps.AuthSteps.토큰_생성_요청;
import static nextstep.member.utils.steps.MemberSteps.내_정보_조회;
import static nextstep.member.utils.steps.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberStepDef implements En {
	@Autowired
	private AcceptanceContext context;

	public MemberStepDef() {
		Given("사용자 정보와 토큰 정보를 생성하고", (DataTable table) ->
				table.asMaps().stream()
						.forEach(params -> {
							String email = params.get("email");
							String password = params.get("password");

							ExtractableResponse<Response> response = 회원_생성_요청(
									email,
									password,
									Integer.parseInt(params.get("age"))
							);
							context.store.put(email, 토큰_생성_요청(email, password).jsonPath().getString("accessToken"));
						})
		);

		Given("사용자 {string}은 어린이다", (String email) -> {
			ExtractableResponse<Response> response = 내_정보_조회(context.store.get(email).toString());

			assertThat(response.jsonPath().getInt("age")).isGreaterThanOrEqualTo(6);
			assertThat(response.jsonPath().getInt("age")).isLessThan(13);
		});

		Given("사용자 {string}은 청소년이다", (String email) -> {
			ExtractableResponse<Response> response = 내_정보_조회(context.store.get(email).toString());

			assertThat(response.jsonPath().getInt("age")).isGreaterThanOrEqualTo(13);
			assertThat(response.jsonPath().getInt("age")).isLessThan(19);
		});

		Given("사용자 {string}은 성인이다", (String email) -> {
			ExtractableResponse<Response> response = 내_정보_조회(context.store.get(email).toString());

			assertThat(response.jsonPath().getInt("age")).isGreaterThanOrEqualTo(19);
		});
	}
}
