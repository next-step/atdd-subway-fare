package nextstep.subway.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.subway.testhelper.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
        });
    }
}
