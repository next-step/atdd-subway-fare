package nextstep.cucumber.utils;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java8.En;
import nextstep.utils.data.DataLoader;
import nextstep.utils.data.DatabaseCleanupExecutor;

/**
 * @author : Rene Choi
 * @since : 2024/02/24
 */
public class DatabaseCleanupStepDef implements En {

	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;
	@Autowired
	private DataLoader dataLoader;

	public DatabaseCleanupStepDef() {
		Before(() -> {
			databaseCleanupExecutor.execute();
			dataLoader.loadData();
		});
	}
}

