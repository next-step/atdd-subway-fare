package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStepDef implements En {
    @Autowired
    private AcceptanceContext acceptanceContext;

    public LineStepDef() {
        DataTableType((Map<String, String> entry) -> new LineRequest(
                entry.get("이름"),
                entry.get("색상"),
                findStation(entry.get("출발역")).getId(),
                findStation(entry.get("도착역")).getId(),
                Integer.parseInt(entry.get("거리")),
                Integer.parseInt(entry.get("소요시간")))
        );

         Given("지하철 노선들을 생성한다", (DataTable dataTable) -> {
            List<LineRequest> lineRequests = dataTable.asList(LineRequest.class);
            for (LineRequest request : lineRequests) {
                ExtractableResponse<Response> lineResponse = RestAssured
                        .given()
                        .when()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .post("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();
                putLine(request, lineResponse);
            }
        });

        And("{string}노선에 {string}에서 {string}까지 거리가 {int}, 소요 시간이 {int}인 지하철 구간을 추가한다", (String line, String upStation, String downStation, Integer distance, Integer duration) -> {
            RestAssured
                    .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new SectionRequest(
                            findStation(upStation).getId(),
                            findStation(downStation).getId(),
                            distance,
                            duration)
                    )
                    .when().log().all()
                    .post("/lines/" + findLine(line).getId() + "/sections")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            addSection(line, upStation, downStation, distance, duration);
        });

        Given("지하철 노선이 등록되어 있다", () -> {
            assertThat(findLine("이호선")).isNotNull();
            assertThat(findLine("신분당선")).isNotNull();
            assertThat(findLine("삼호선")).isNotNull();
        });

        Given("지하철 노선에 지하철역이 등록되어있다", () -> {
            assertThat(findLine("이호선").getSections().allStations()).hasSize(2);
            assertThat(findLine("신분당선").getSections().allStations()).hasSize(2);
            assertThat(findLine("삼호선").getSections().allStations()).hasSize(3);
        });
    }

    private Station findStation(String name) {
        return acceptanceContext.findStation(name);
    }

    private Station findStationById(Long id) {
        return acceptanceContext.findStationById(id);
    }

    private void putLine(LineRequest request, ExtractableResponse<Response> lineResponse) {
        LineResponse responseObj = lineResponse.as(LineResponse.class);
        acceptanceContext.putLine(request.getName(), new Line(responseObj.getId(), responseObj.getName(), responseObj.getColor(), findStationById(request.getUpStationId()), findStationById(request.getDownStationId()), request.getDistance(), request.getDuration()));
    }

    private void addSection(String line, String upStation, String downStation, int distance, int duration) {
        Section newSection = new Section(findLine(line), findStation(upStation), findStation(downStation), distance, duration);
        acceptanceContext.addSection(line, newSection);
    }

    private Line findLine(String name) {
        return acceptanceContext.findLine(name);
    }
}
