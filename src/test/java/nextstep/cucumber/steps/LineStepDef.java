package nextstep.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.controller.dto.LineResponse;
import nextstep.subway.controller.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.line.LineSteps.라인_생성요청;
import static nextstep.subway.acceptance.sections.SectionSteps.구간_생성요청;

public class LineStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public LineStepDef() {
        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(param -> {
                        ExtractableResponse<Response> response = 라인_생성요청(
                                param.get("name"),
                                param.get("color"),
                                ((StationResponse) context.store.get(param.get("upStation"))).getId(),
                                ((StationResponse) context.store.get(param.get("downStation"))).getId(),
                                Long.parseLong(param.get("distance")),
                                Long.parseLong(param.get("duration")),
                                Long.parseLong(param.get("additionalFare"))
                        );

                        context.store.put(param.get("name"), (new ObjectMapper()).convertValue(response.jsonPath().get(), LineResponse.class));
                    });
        });

        Given("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(param -> {
                        String lineName = param.get("lineName");
                        LineResponse line = (LineResponse) context.store.get(lineName);

                        구간_생성요청(line.getId(),
                                ((StationResponse) context.store.get(param.get("upStation"))).getId(),
                                ((StationResponse) context.store.get(param.get("downStation"))).getId(),
                                Long.parseLong(param.get("distance")),
                                Long.parseLong(param.get("duration")));
                    });
        });
    }
}
