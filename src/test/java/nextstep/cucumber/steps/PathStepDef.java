package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.createToken;
import static nextstep.subway.acceptance.LineSteps.노선이_생성되어_있다;
import static nextstep.subway.acceptance.LineSteps.추가요금이_있는_노선이_생성되어_있다;
import static nextstep.subway.acceptance.SectionSteps.구간을_등록한다;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    public static final String PATHS = "/paths";

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {

        Given("지하철역들을 생성 요청하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(m -> {
                        final String name = m.get("name");
                        final Long stationId = 지하철역_생성_요청(name)
                                .as(StationResponse.class).getId();
                        context.store.put(name, stationId);
                    });
        });

        Given("노선들을 생성 요청하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final String name = map.get("name");
                final String color = map.get("color");
                final Long upStationId = (Long) context.store.get(map.get("upStation"));
                final Long downStationId = (Long) context.store.get(map.get("downStation"));
                final int distance = Integer.parseInt(map.get("distance"));
                final int duration = Integer.parseInt(map.get("duration"));

                final Long lineId = 노선이_생성되어_있다(name, color, upStationId, downStationId, distance, duration)
                        .as(LineResponse.class).getId();
                context.store.put(name, lineId);
            }
        });

        Given("추가요금이 있는 노선들을 생성 요청하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final String name = map.get("name");
                final String color = map.get("color");
                final Long upStationId = (Long) context.store.get(map.get("upStation"));
                final Long downStationId = (Long) context.store.get(map.get("downStation"));
                final int distance = Integer.parseInt(map.get("distance"));
                final int duration = Integer.parseInt(map.get("duration"));
                final int additionalFee = Integer.parseInt(map.get("additionalFee"));

                final Long lineId = 추가요금이_있는_노선이_생성되어_있다(name, color, upStationId, downStationId, distance, duration, additionalFee)
                        .as(LineResponse.class).getId();
                context.store.put(name, lineId);
            }
        });

        Given("구간을 등록하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final Long lineId = (Long) context.store.get(map.get("lineName"));
                final Long upStationId = (Long) context.store.get(map.get("upStation"));
                final Long downStationId = (Long) context.store.get(map.get("downStation"));
                final int distance = Integer.parseInt(map.get("distance"));
                final int duration = Integer.parseInt(map.get("duration"));

                context.response = 구간을_등록한다(lineId, upStationId, downStationId, distance, duration);
            }
        });

        When("{string}과 {string} 사이의 최소거리 경로 조회를 요청하면", (String source, String target) -> {
            Long sourceId = (Long) context.store.get(source);
            Long targetId = (Long) context.store.get(target);

            context.response = RestAssured.given().log().all()
                    .queryParam("source", sourceId)
                    .queryParam("target", targetId)
                    .queryParam("type", "DISTANCE")
                    .when().get(PATHS)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        When("{string}과 {string} 사이의 최소시간 경로 조회를 요청하면", (String source, String target) -> {
            Long sourceId = (Long) context.store.get(source);
            Long targetId = (Long) context.store.get(target);

            context.response = RestAssured.given().log().all()
                    .queryParam("source", sourceId)
                    .queryParam("target", targetId)
                    .queryParam("type", "DURATION")
                    .when().get(PATHS)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        When("{string}사용자가 {string}과 {string} 사이의 최소거리 경로 조회를 요청하면", (String email, String source, String target) -> {
            Long sourceId = (Long) context.store.get(source);
            Long targetId = (Long) context.store.get(target);

            final String jwtToken = createToken(email);

            context.response = RestAssured.given().log().all()
                    .auth().oauth2(jwtToken)
                    .queryParam("source", sourceId)
                    .queryParam("target", targetId)
                    .queryParam("type", "DISTANCE")
                    .when().get(PATHS)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        When("어린이 사용자가 {string}과 {string} 사이의 최소거리 경로 조회를 요청하면", (String source, String target) -> {
            Long sourceId = (Long) context.store.get(source);
            Long targetId = (Long) context.store.get(target);

            context.response = RestAssured.given().log().all()
                    .queryParam("source", sourceId)
                    .queryParam("target", targetId)
                    .queryParam("type", "DISTANCE")
                    .when().get(PATHS)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        Then("{string} 지하철역을_리턴한다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        Then("총 거리 {int}km와 총 소요 시간 {int}을 리턴한다", (Integer distance, Integer duration) -> {
            final PathResponse pathResponse = context.response.as(PathResponse.class);
            final int expectDistance = pathResponse.getDistance();
            final int expectDuration = pathResponse.getDuration();

            assertThat(expectDistance).isEqualTo(distance);
            assertThat(expectDuration).isEqualTo(duration);
        });

        Then("지하철 이용 요금 {int}원을 리턴한다", (Integer fee) -> {
            final PathResponse pathResponse = context.response.as(PathResponse.class);
            final int expectDistance = pathResponse.getFare();

            assertThat(expectDistance).isEqualTo(fee);
        });
    }
}
