package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.common.utils.DataLoader;
import nextstep.common.utils.DatabaseCleanup;
import nextstep.cucumber.AcceptanceContext;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.acceptance.TokenSteps;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeStepDef implements En {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private AcceptanceContext context;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
            dataLoader.loadData();

            MemberSteps.회원_생성_요청("children@test.com", "q1234", 10);
            context.store.put("children_token", TokenSteps.토큰_생성요청("children@test.com", "q1234").as(TokenResponse.class).getAccessToken());

            MemberSteps.회원_생성_요청("teenager@test.com", "q1234", 15);
            context.store.put("teenager_token", TokenSteps.토큰_생성요청("teenager@test.com", "q1234").as(TokenResponse.class).getAccessToken());

            MemberSteps.회원_생성_요청("adult@test.com", "q1234", 20);
            context.store.put("adult_token", TokenSteps.토큰_생성요청("adult@test.com", "q1234").as(TokenResponse.class).getAccessToken());

        });
    }
}
