package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.api.LineApiHelper;
import nextstep.common.api.SectionApiHelper;
import nextstep.cucumber.AcceptanceContext;
import nextstep.line.application.dto.LineResponse;
import nextstep.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class LineStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public LineStepDef() {
        Given("지하철 노선들을 생성 요청하고", (final DataTable table) -> {
            final List<Map<String, String>> maps = table.asMaps();
            maps.forEach(data -> {
                final String lineName = data.get("name");
                final String lineColor = data.get("color");
                final StationResponse upStation = context.store.get(data.get("upStation"), StationResponse.class);
                final StationResponse downStation = context.store.get(data.get("downStation"), StationResponse.class);
                final int distance = Integer.parseInt(data.get("distance"));
                final int duration = Integer.parseInt(data.get("duration"));
                final ExtractableResponse<Response> response = LineApiHelper.createLine(lineName, lineColor, upStation.getId(), downStation.getId(), distance, duration);
                context.store.put(lineName, response.as(LineResponse.class));
            });
        });
        Given("지하철 구간을 등록 요청하고", (final DataTable table) -> {
            final List<Map<String, String>> maps = table.asMaps();
            maps.forEach(data -> {
                final String lineName = data.get("lineName");
                final StationResponse upStation = context.store.get(data.get("upStation"), StationResponse.class);
                final StationResponse downStation = context.store.get(data.get("downStation"), StationResponse.class);
                final int distance = Integer.parseInt(data.get("distance"));
                final int duration = Integer.parseInt(data.get("duration"));
                final LineResponse line = context.store.get(lineName, LineResponse.class);
                SectionApiHelper.createSection(line.getId(), upStation.getId(), downStation.getId(), distance, duration);
            });
        });
    }

}
