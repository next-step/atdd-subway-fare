package nextstep.cucumber.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseDef implements En {
	@Autowired
	private AcceptanceContext context;

	public ResponseDef() {
		Then("{string}라는 메시지를 반환한다", (String message) -> {
			assertThat(context.response.body().jsonPath().getString("message")).isEqualTo(message);
		});
	}
}
