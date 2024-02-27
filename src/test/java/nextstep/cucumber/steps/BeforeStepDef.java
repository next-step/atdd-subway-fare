package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.DataSaver;
import nextstep.DatabaseCleaner;
import nextstep.auth.acceptance.AuthSteps;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.DataSaver.*;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @LocalServerPort
    private int port;

    @Autowired
    private DataSaver dataSaver;

    @Autowired
    private AcceptanceContext context;

    public BeforeStepDef() {
        Before(() -> {
            if (RestAssured.port == UNDEFINED_PORT) {
                RestAssured.port = port;
            }
            databaseCleaner.clear();
            dataSaver.saveChildMember();
            dataSaver.saveTeenagerMember();
            dataSaver.saveAdultMember();

            String childAccessToken = AuthSteps.로그인_요청(MEMBER_CHILD_EMAIL, MEMBER_CHILD_PASSWORD)
                    .as(TokenResponse.class).getAccessToken();
            context.store.put("childAccessToken", childAccessToken);

            String teenagerAccessToken = AuthSteps.로그인_요청(MEMBER_TEENAGER_EMAIL, MEMBER_TEENAGER_PASSWORD)
                    .as(TokenResponse.class).getAccessToken();
            context.store.put("teenagerAccessToken", teenagerAccessToken);

            String adultAccessToken = AuthSteps.로그인_요청(MEMBER_ADULT_EMAIL, MEMBER_ADULT_PASSWORD)
                    .as(TokenResponse.class).getAccessToken();
            context.store.put("adultAccessToken", adultAccessToken);
        });
    }
}
