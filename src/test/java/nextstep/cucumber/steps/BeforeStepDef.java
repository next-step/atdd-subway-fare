package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.utils.DataLoader;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    DataLoader dataLoader;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
            dataLoader.loadData();
        });
    }
}
