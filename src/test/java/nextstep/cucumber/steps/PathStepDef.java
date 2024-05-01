package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.step.PathSteps.지하철_경로_조회_요청;
import static nextstep.subway.acceptance.step.PathSteps.지하철역_경로_조회_응답에서_경로_거리_추출;
import static nextstep.subway.acceptance.step.PathSteps.지하철역_경로_조회_응답에서_경로_시간_추출;
import static nextstep.subway.acceptance.step.PathSteps.지하철역_경로_조회_응답에서_역_이름_목록_추출;
import static nextstep.subway.acceptance.step.SectionSteps.지하철_구간_등록_요청;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.response.LineResponse;
import nextstep.subway.application.dto.response.StationResponse;
import nextstep.subway.domain.enums.PathSearchType;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;


    public PathStepDef() {

        Given("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.forEach(param -> {
                String lineName = param.get("lineName");
                Map<String, Object> params = new HashMap<>();
                params.put("upStationId",
                    context.getValueFromStore(param.get("upStation"), StationResponse.class).getId().toString());
                params.put("downStationId",
                    context.getValueFromStore(param.get("downStation"), StationResponse.class).getId().toString());
                params.put("distance", param.get("distance"));
                params.put("duration", param.get("duration"));
                LineResponse line = context.getValueFromStore(lineName, LineResponse.class);
                지하철_구간_등록_요청(line.getId(), params);
            });
        });

        When("{string}에서 {string}까지의 {string} 기준으로 경로 조회를 요청하면",
            (String sourceStationName, String targetStationName, String type) -> {
                Long sourceId = ((StationResponse) context.store.get(sourceStationName)).getId();
                Long targetId = ((StationResponse) context.store.get(targetStationName)).getId();
                PathSearchType searchType = getPathSearchType(type);
                context.response = 지하철_경로_조회_요청(sourceId, targetId, searchType);
            });
        Then("{string} 기준 {string} 경로를 응답", (String type, String path) -> {
            List<String> stationNames = Arrays.asList(path.split(","));
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(지하철역_경로_조회_응답에서_역_이름_목록_추출(context.response))
                    .containsExactlyElementsOf(stationNames);
            });
        });

        And("총 거리 {long}와 소요 시간 {long}을 함께 응답함", (Long distance, Long duration) -> {
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(지하철역_경로_조회_응답에서_경로_거리_추출(context.response))
                    .isEqualTo(distance);
                softAssertions.assertThat(지하철역_경로_조회_응답에서_경로_시간_추출(context.response))
                    .isEqualTo(duration);
            });
        });
    }

    private PathSearchType getPathSearchType(String type) {
        if (type.equals("최소 시간")) {
            return PathSearchType.DURATION;
        }
        if (type.equals("최소 거리")) {
            return PathSearchType.DISTANCE;
        }
        throw new IllegalArgumentException(String.format("do not match PathSearchType: %s", type));
    }

}
