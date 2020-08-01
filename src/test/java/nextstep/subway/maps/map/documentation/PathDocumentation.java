package nextstep.subway.maps.map.documentation;

import nextstep.subway.Documentation;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.maps.map.application.MapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.map.ui.MapController;
import nextstep.subway.maps.station.dto.StationResponse;
import nextstep.subway.members.member.domain.LoginMember;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
    }

    @Test
    void findPath() {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);
        params.put("type", PathType.ARRIVAL_TIME);
        params.put("time", "202007011700");
        List<StationResponse> stations = Lists.list(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "교대역", LocalDateTime.now(), LocalDateTime.now())
        );
        when(mapService.findPath(any(LoginMember.class), anyLong(), anyLong(), any(PathType.class), any()))
                .thenReturn(new PathResponse(stations, 20, 10, 10));

        given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                params(params).
                when().
                get("/paths").
                then().
                log().all().
                apply(document("paths/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("time").description("경로 출발 시간"),
                                parameterWithName("type").description("최단 시간 / 최단 거리")),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철 역 정보"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철 역 아이디"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철 역 이름"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 거리"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("지하철 요금")
                        )
                )).
                extract();
    }
}
