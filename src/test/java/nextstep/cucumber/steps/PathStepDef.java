package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathStep.지하철_경로_조회;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.utils.ResponseUtils.응답에서_id_조회;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
         Given("지하철역 생성을 요청하고", (DataTable table) -> {
             List<Map<String, String>> maps = table.asMaps();
             maps.forEach(it ->
                 context.store.put(
                     it.get("name"),
                     응답에서_id_조회(지하철역_생성_요청(it.get("name")))
                 )
             );
         });

         And("지하철 노선 생성을 요청하고", (DataTable table) -> {
             List<Map<String, String>> maps = table.asMaps();
             maps.forEach(it -> context.store.put(
                 it.get("name"),
                 응답에서_id_조회(
                     지하철_노선_생성_요청(
                         it.get("name"),
                         it.get("color"),
                         Long.parseLong(context.store.get(it.get("upStation")).toString()),
                         Long.parseLong(context.store.get(it.get("downStation")).toString()),
                         Integer.parseInt(it.get("distance")),
                         Integer.parseInt(it.get("duration")),
                         Integer.parseInt(it.get("extraFare"))
                     )
                 )
             ));
         });

         And("지하철 노선에 구간 생성을 요청한다", (DataTable table) -> {
             List<Map<String, String>> maps = table.asMaps();
             maps.forEach(it -> {
                 지하철_노선에_지하철_구간_생성_요청(
                     Long.parseLong(context.store.get(it.get("line")).toString()),
                     createSectionCreateParams(
                         context.store.get(it.get("upStation")).toString(),
                         context.store.get(it.get("downStation")).toString(),
                         it.get("distance"),
                         it.get("duration")
                     ));
             });
         });

        When("{string} 과 {string} 경로를 조회하면,", (String from, String to) -> {
            context.response = 지하철_경로_조회(
                Long.parseLong(context.store.get(from).toString()),
                Long.parseLong(context.store.get(to).toString()),
                PathSearchType.DISTANCE
            );
        });

        When("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청", (String from, String to) -> {
            context.response = 지하철_경로_조회(
                Long.parseLong(context.store.get(from).toString()),
                Long.parseLong(context.store.get(to).toString()),
                PathSearchType.DURATION
            );
        });

        Then("{string} 과 {string} 간 경로가 조회 된다.", (String from, String to) -> {
            final List<Station> stations = context.response.jsonPath().getList("stations", Station.class);

            assertThat(stations.stream().mapToLong(Station::getId)).containsAll(List.of(
                Long.parseLong(context.store.get(from).toString()),
                Long.parseLong(context.store.get(to).toString())
            ));
        });

        Then("거리는 {int}이다.", (Integer distance) -> {
           assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(distance);
        });

        Then("시간은 {int}이다.", (Integer duration) -> {
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(duration);
        });

        Then("요금은 {int}원이다.",(Integer fare) -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(fare);
        });

        Then("경로가 조회가 실패한다.", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        });
    }


    private Map<String, String> createSectionCreateParams(String upStationId, String downStationId, String distance, String duration) {
        final Map<String, String> params = new HashMap<>();

        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("duration", duration);

        return params;
    }
}
