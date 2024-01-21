package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java8.En;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("지하철역들을 생성하고", (DataTable table) -> {
            List<String> names = table.asList();
            names.forEach(name -> context.add(지하철역_생성_요청(name)));
        });
        Given("지하철 노선을 생성하고", (DataTable table) -> {
            List<LineRequest> requests = table.asList(LineRequest.class);
            createLines(requests);
        });
        Given("{string}에 지하철역을 추가하고", (String lineName, DataTable table) -> {
            Long lineId = context.get(lineName);
            List<SectionRequest> requests = table.asList(SectionRequest.class);
            addSectionInLine(lineId, requests);
        });
        When("{string}과 {string} 사이 경로를 조회하면", (String upStation, String downStation) -> {
            Long upStationId = context.get(upStation);
            Long downStationId = context.get(downStation);
            context.response = PathSteps.두_역의_최단_거리_경로_조회를_요청(
                    upStationId, downStationId
            );
        });

        Then("{string}-{string}-{string} 경로가 조회된다.", (String upStation, String middleStation, String downStation)
                -> assertThat(context.response.jsonPath().getList("stations.name", String.class))
                .containsExactly(upStation, middleStation, downStation));
    }

    private void createLines(List<LineRequest> requests) {
        requests.forEach(request -> {
            context.add(지하철_노선_생성_요청(request));
        });
    }

    private void addSectionInLine(Long lineId, List<SectionRequest> requests) {
        requests.forEach(request -> {
            context.add(지하철_노선에_지하철_구간_생성_요청(lineId, request));
        });
    }


    @DataTableType
    public LineRequest lineRequestEntryTransformer(Map<String, String> entry) {
        return new LineRequest(
                entry.get("name"),
                entry.get("color"),
                context.get(entry.get("upStation")),
                context.get(entry.get("downStation")),
                Integer.parseInt(entry.get("distance"))
        );
    }

    @DataTableType
    public SectionRequest sectionRequestEntryTransformer(Map<String, String> entry) {
        return new SectionRequest(
                context.get(entry.get("upStation")),
                context.get(entry.get("downStation")),
                Integer.parseInt(entry.get("distance"))
        );
    }
}
