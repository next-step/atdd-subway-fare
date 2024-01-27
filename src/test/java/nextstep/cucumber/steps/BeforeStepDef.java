package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.utils.DataLoader;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private AcceptanceContext acceptanceContext;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
            dataLoader.loadData();
            acceptanceContext.clear();
        });
    }
}
