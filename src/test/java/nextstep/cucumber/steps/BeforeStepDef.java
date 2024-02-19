package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.core.DatabaseCleaner;
import nextstep.utils.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private DataLoader dataLoader;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleaner.clear();
            dataLoader.loadData();
        });
    }
}
