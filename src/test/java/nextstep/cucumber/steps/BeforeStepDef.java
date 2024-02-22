package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.DatabaseCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.UNDEFINED_PORT;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @LocalServerPort
    private int port;

    public BeforeStepDef() {
        Before(() -> {
            if (RestAssured.port == UNDEFINED_PORT) {
                RestAssured.port = port;
            }
            databaseCleaner.clear();
        });
    }
}
