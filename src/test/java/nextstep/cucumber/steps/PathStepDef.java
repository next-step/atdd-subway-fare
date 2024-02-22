package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    public static final String PATHS = "/paths";

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string} 사이의 경로 조회를 요청하면", (String source, String target) -> {
            Long sourceId = (Long) context.store.get(source);
            Long targetId = (Long) context.store.get(target);

            context.response = RestAssured.given().log().all()
                    .queryParam("source", sourceId)
                    .queryParam("target", targetId)
                    .when().get(PATHS)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        When("{string} 지하철역을_리턴한다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });
    }

}
