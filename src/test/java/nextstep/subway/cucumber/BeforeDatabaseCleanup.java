package nextstep.subway.cucumber;

import io.cucumber.java8.En;
import nextstep.subway.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeDatabaseCleanup implements En {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    public BeforeDatabaseCleanup() {
        Before(() -> {
            databaseCleanup.execute();
        });
    }
}
