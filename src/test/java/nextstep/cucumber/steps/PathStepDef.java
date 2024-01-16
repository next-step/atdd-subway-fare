package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.domain.PathRequestType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;


public class PathStepDef implements En {


    @Autowired
    private AcceptanceContext context;
    ExtractableResponse<Response> response;

    public PathStepDef() {

        Given("지하철역들을 생성하고", (DataTable table) -> {
            List<String> names = table.asList();
            names.forEach(name -> context.add(지하철역_생성_요청(name)));
        });

        Given("지하철 노선들을 생성하고", (DataTable table) -> {
            List<Map<String, String>> lines = table.asMaps();
            lines.forEach(this::createLine);
        });

        Given("{string}에 지하철 역을 추가하고", (String lineName, DataTable table) -> {
            Long lineId = context.get(lineName);
            table.asMaps().forEach(add -> addStationInLine(add, lineId));
        });
        When("{string}과 {string} 사이 경로를 조회하면", (String upStation, String downStation)
                -> response = PathSteps.두_역의_거리_경로_조회를_요청(
                context.get(upStation), context.get(downStation), PathRequestType.DISTANCE
        ));
        Then("{string}-{string}-{string} 경로가 조회된다", (String upStation, String middleStation, String downStation)
                -> assertThat(response.jsonPath().getList("stations.name", String.class))
                .containsExactly(upStation, middleStation, downStation));
    }

    private void addStationInLine(Map<String, String> add, Long lineId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", context.get(add.get("upStation")) + "");
        params.put("downStationId", context.get(add.get("downStation")) + "");
        params.put("distance", add.get("distance"));
        params.put("duration", add.get("duration"));
        지하철_노선에_지하철_구간_생성_요청(lineId, params);
    }

    private void createLine(Map<String, String> line) {
        Map<String, String> params = new HashMap<>();
        params.put("name", line.get("name"));
        params.put("color", line.get("color"));
        params.put("upStationId", context.get(line.get("upStation")) + "");
        params.put("downStationId", context.get(line.get("downStation")) + "");
        params.put("distance", line.get("distance"));
        params.put("duration", line.get("duration"));
        params.put("surcharge", line.get("surcharge"));
        var response = 지하철_노선_생성_요청(params);
        context.add(response);
    }
}
