package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthStepDef implements En {
    @Autowired
    DatabaseCleanup databaseCleanup;
    ExtractableResponse<Response> response;

    public AuthStepDef() {
        Before(() -> {
            databaseCleanup.execute();
        });

    }
}
