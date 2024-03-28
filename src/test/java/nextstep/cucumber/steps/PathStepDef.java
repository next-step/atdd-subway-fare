package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    ExtractableResponse<Response> response;
    String token = "";

    @Autowired
    private AcceptanceContext acceptanceContext;

    public PathStepDef() {
        When("{string}부터 {string}까지의 {string} 경로를 조회하면", (String source, String target, String type) -> {
            PathType pathType = type.equals("최단 거리") ? PathType.DISTANCE : PathType.DURATION;
            response = RestAssured
                    .given().log().all()
                    .auth().oauth2(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("source", findStation(source).getId())
                    .param("target", findStation(target).getId())
                    .param("type", pathType)
                    .when()
                    .get("/paths")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        Then("{string} 경로와 거리 {int}km, 소요시간 {int}분으로 응답한다.", (String stations, Integer distance, Integer duration) -> {
            String[] stationArr = stations.replaceAll(" ", "").split(",");
            List<Long> stationIds = Arrays.stream(stationArr).map(s -> findStation(s).getId()).collect(Collectors.toList());

            assertThat(response.jsonPath().getList("stations.id", Long.class)).isEqualTo(stationIds);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        });

        And("지하철 이용 요금 {int}원도 함께 응답한다", (Integer fare) -> {
            assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
        });


        When("회원 {string}이 {string}부터 {string}까지의 {string} 경로를 조회하면", (String email, String source, String target, String type) -> {
            PathType pathType = type.equals("최단 거리") ? PathType.DISTANCE : PathType.DURATION;
            String token = findMemberToken(email);
            response = RestAssured
                    .given().log().all()
                    .auth().oauth2(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("source", findStation(source).getId())
                    .param("target", findStation(target).getId())
                    .param("type", pathType)
                    .when()
                    .get("/paths/member")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });
    }

    private Station findStation(String name) {
        return acceptanceContext.findStation(name);
    }

    private String findMemberToken(String email) {
        return acceptanceContext.findMemberToken(email);
    }
}
