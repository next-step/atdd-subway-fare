package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.support.DataLoader;
import nextstep.support.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDefinitions implements En {
  @Autowired private DatabaseCleanup databaseCleanup;
  @Autowired private DataLoader dataLoader;

  public BeforeStepDefinitions() {
    Before(
        () -> {
          databaseCleanup.execute();
          dataLoader.loadData();
        });
  }
}
