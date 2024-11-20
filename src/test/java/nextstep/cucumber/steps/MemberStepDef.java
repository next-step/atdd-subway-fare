package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.member.acceptance.MemberSteps;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MemberStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    @Given("연령별 회원을 등록하고")
    public void 연령별_회원_등록(DataTable dataTable) {
        List<Map<String, String>> maps = dataTable.asMaps();

        maps.forEach(e -> {
            MemberSteps.회원_생성_요청(e.get("email"), e.get("password"), Integer.parseInt(e.get("age")));
        });
    }
}
