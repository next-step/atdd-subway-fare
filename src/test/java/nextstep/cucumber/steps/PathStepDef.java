package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.presentation.TokenRequest;
import nextstep.cucumber.SharedContext;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;
    private String authToken;


    public PathStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;

        Given("별도 할인 요금 정책이 없는 사용자가", () -> {
            authToken = 로그인_및_토큰_얻기("adult@email.com", "password");
        });

        Given("청소년 사용자가", () -> {
            authToken = 로그인_및_토큰_얻기("teenager@email.com", "password");
        });

        Given("어린이 사용자가", () -> {
            authToken = 로그인_및_토큰_얻기("child@email.com", "password");
        });

        When("{string}에서 {string}까지의 {string} 경로를 조회 하면", (String source, String target, String pathType) -> {
            Long sourceStationId = sharedContext.getStationId(source);
            Long targetStationId = sharedContext.getStationId(target);
            경로_찾기(sourceStationId, targetStationId, pathType);
        });

        Then("다음과 같은 경로와 거리 및 요금을 응답받는다", (DataTable dataTable) -> {
            검증_경로_및_값_확인(dataTable, "distance");
        });

        Then("다음과 같은 경로와 시간 및 요금을 응답받는다", (DataTable dataTable) -> {
            검증_경로_및_값_확인(dataTable, "duration");
        });
    }
    private String 로그인_및_토큰_얻기(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        return loginResponse.jsonPath().getString("accessToken");
    }

    private void 경로_찾기(Long sourceStationId, Long targetStationId, String pathType) {
        String type = pathType.equals("최소시간") ? "DURATION" : "DISTANCE";
        response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(authToken)
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 검증_경로_및_값_확인(DataTable dataTable, String valueType) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            검증_경로(row.get("경로"));
            검증_거리_또는_시간(row, valueType);
            검증_요금(Integer.parseInt(row.get("요금")));
        }
    }

    private void 검증_경로(String expectedPath) {
        String actualPath = 실제_경로_추출();
        assertThat(actualPath).isEqualTo(expectedPath);
    }

    private void 검증_거리_또는_시간(Map<String, String> row, String valueType) {
        int expectedValue = valueType.equals("distance")
                ? Integer.parseInt(row.get("거리"))
                : Integer.parseInt(row.get("시간"));
        int actualValue = response.jsonPath().getInt("amount");
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    private void 검증_요금(int expectedFare) {
        int actualFare = response.jsonPath().getInt("fare");
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    private String 실제_경로_추출() {
        List<String> actualStations = response.jsonPath().getList("stations.name");
        return String.join(" - ", actualStations);
    }
}
