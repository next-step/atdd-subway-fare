package nextstep.subway.cucumber.steps;

import static nextstep.subway.acceptance.LineSteps.경로_역_목록_조회됨;
import static nextstep.subway.acceptance.LineSteps.경로_전체_거리_조회됨;
import static nextstep.subway.acceptance.LineSteps.경로_전체_시간_조회됨;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathAcceptanceSteps.경로_전체_요금_조회됨;
import static nextstep.subway.acceptance.PathAcceptanceSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.cucumber.AcceptanceContext;
import nextstep.subway.cucumber.CucumberTest;
import nextstep.subway.cucumber.steps.dto.PathStepResponse;
import nextstep.subway.domain.PathType;
import org.springframework.beans.factory.annotation.Autowired;

public class PathStepDef extends CucumberTest implements En {

    @Autowired
    private AcceptanceContext context;
    private ExtractableResponse<Response> response;

    public PathStepDef() {
        Given("^지하철역을 생성하고$", (final DataTable dataTable) -> {
            final List<String> stationNames = dataTable.asList();
            for (final String stationName : stationNames) {
                final Long id = 지하철역_생성_요청(stationName).jsonPath().getLong("id");

                context.put(stationName, id + "");
            }
        });

        And("^지하철 노선을 생성하고$", (final DataTable dataTable) -> {
            final List<Map<String, String>> lines = dataTable.asMaps();
            for (final Map<String, String> line : lines) {
                final Map<String, String> lineParam = createLineParam(line);
                final Long id = LineSteps.지하철_노선_생성_요청(lineParam).jsonPath().getLong("id");

                context.put(line.get("name"), id + "");
            }
        });

        And("{string}에 구간을 생성한다", (final String lineName, final DataTable dataTable) -> {
            final Long lineId = context.getLongValue(lineName);
            final Map<String, String> section = dataTable.asMap();
            final Map<String, String> sectionParam = createSectionParam(section);

            지하철_노선에_지하철_구간_생성_요청(lineId, sectionParam);
        });

        When("{string}에서 {string}까지 최단 경로 조회 요청하면", (final String source, final String target) -> {
            final Long sourceId = context.getLongValue(source);
            final Long targetId = context.getLongValue(target);

            response = 두_역의_경로_조회를_요청(given(), sourceId, targetId, PathType.DISTANCE);
        });

        DataTableType((final Map<String, String> pathStepResponse) -> new PathStepResponse(
            pathStepResponse.get("stations").split(","),
            Integer.parseInt(pathStepResponse.get("distance")),
            Integer.parseInt(pathStepResponse.get("duration")),
            Integer.parseInt(pathStepResponse.get("fare"))
        ));

        Then("^최단 경로 응답와 총 거리, 소요 시간, 요금정보를 조회한다$", (final PathStepResponse pathStepResponse) -> {
            경로_역_목록_조회됨(response, pathStepResponse.getStations());
            경로_전체_거리_조회됨(response, pathStepResponse.getDistance());
            경로_전체_시간_조회됨(response, pathStepResponse.getDuration());
            경로_전체_요금_조회됨(response, pathStepResponse.getFare());
        });
    }

    private Map<String, String> createLineParam(final Map<String, String> line) {
        final Map<String, String> lineParam = new HashMap<>();
        lineParam.put("name", line.get("name"));
        lineParam.put("color", line.get("color"));
        lineParam.put("upStationId", context.get(line.get("upStation")));
        lineParam.put("downStationId", context.get(line.get("downStation")));
        lineParam.put("distance", line.get("distance"));
        lineParam.put("duration", line.get("duration"));
        lineParam.put("extraCharge", line.get("extraCharge"));
        return lineParam;
    }

    private Map<String, String> createSectionParam(final Map<String, String> section) {
        final Map<String, String> sectionParam = new HashMap<>();
        sectionParam.put("upStationId", context.get(section.get("upStation")));
        sectionParam.put("downStationId", context.get(section.get("downStation")));
        sectionParam.put("distance", section.get("distance"));
        sectionParam.put("duration", section.get("duration"));
        return sectionParam;
    }

    private RequestSpecification given() {
        return RestAssured.given().log().all();
    }
}
