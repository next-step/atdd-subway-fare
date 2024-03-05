package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.utils.DatabaseCleaner;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleaner databaseCleanup;
    @Autowired
    private PathAcceptanceContext pathCxt;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.cleanUp();
            pathCxt.cleanUp();
        });
    }
}