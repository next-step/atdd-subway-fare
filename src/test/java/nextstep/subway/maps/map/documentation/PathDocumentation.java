package nextstep.subway.maps.map.documentation;

import com.google.common.collect.Lists;
import nextstep.subway.Documentation;
import nextstep.subway.maps.map.application.MapService;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.map.ui.MapController;
import nextstep.subway.maps.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@WebMvcTest(controllers = {MapController.class})
public class PathDocumentation extends Documentation {
    @Autowired
    private MapController mapController;
    @MockBean
    private MapService mapService;

    @Test
    void findPath() {
        List<StationResponse> stations = Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now())
        );
        PathResponse pathResponse = new PathResponse(stations, 20, 10);

        when(mapService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DISTANCE").
                then().
                log().all().
                apply(document("paths/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("type").description("조회 기준")),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 아이디"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간(분)"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리(km)")))).
                extract();
    }
}
