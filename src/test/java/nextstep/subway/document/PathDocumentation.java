package nextstep.subway.document;

import nextstep.subway.domain.service.path.StationPathSearchRequestType;
import nextstep.subway.service.StationPathService;
import nextstep.subway.service.dto.StationPathResponse;
import nextstep.subway.service.dto.StationResponse;
import nextstep.utils.Documentation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static nextstep.subway.acceptance.PathSteps.지하철_경로_조회;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private StationPathService stationPathService;

    private Long sourceId = 1L;
    private Long targetId = 1L;

    @Test
    void path() {
        final StationResponse station1 = new StationResponse();
        station1.setId(1L);
        station1.setName("강남역");

        final StationResponse station2 = new StationResponse();
        station2.setId(2L);
        station2.setName("역삼역");

        final StationPathResponse pathResponse = StationPathResponse.builder()
                .stations(List.of(station1, station2))
                .fee(BigDecimal.TEN)
                .distance(BigDecimal.TEN).build();

        when(stationPathService.searchStationPath(any(), anyLong(), anyLong(), any())).thenReturn(pathResponse);

        var doc = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath(".distance").description("지하철 경로 전체 거리"),
                        fieldWithPath(".duration").description("지하철 경로 전체 소요 시간"),
                        fieldWithPath(".fee").description("지하철 경로 전체 요금"),
                        fieldWithPath(".stations[]").description("지하철 경로 역 목록"),
                        fieldWithPath(".stations[].id").description("지하철 역 id"),
                        fieldWithPath(".stations[].name").description("지하철 역 이름")
                )
        );

        지하철_경로_조회(spec, doc, sourceId, targetId, StationPathSearchRequestType.DISTANCE);
    }
}
