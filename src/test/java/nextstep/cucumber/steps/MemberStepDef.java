package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

public class MemberStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public MemberStepDef() {
        Given("회원을 등록하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final String email = map.get("email");
                final String password = map.get("password");
                final Integer age = Integer.valueOf(map.get("age"));

                context.response = 회원_생성_요청(email, password, age);
            }
        });
    }

}
