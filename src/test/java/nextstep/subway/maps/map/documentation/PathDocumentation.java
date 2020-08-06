package nextstep.subway.maps.map.documentation;

import io.restassured.http.ContentType;
import nextstep.subway.Documentation;
import nextstep.subway.maps.map.application.MapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.map.ui.MapController;
import nextstep.subway.maps.station.dto.StationResponse;
import nextstep.subway.members.member.domain.LoginMember;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@WebMvcTest(controllers = {MapController.class})
public class PathDocumentation extends Documentation {

    @MockBean
    private MapService mapService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
    }

    @Test
    void findPathDocumentation() {

        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("source", 1L);
        requestParam.put("target", 2L);
        requestParam.put("type", PathType.DISTANCE);

        List<StationResponse> stations = Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "강낭콩", LocalDateTime.now(), LocalDateTime.now())
        );

        PathResponse pathResponse = new PathResponse(stations, 20, 10, 1250);
        when(mapService.findPath(any(LoginMember.class), any())).thenReturn(pathResponse);

        given().log().all()
                .params(requestParam)
                .contentType(ContentType.JSON)
                .get("/paths")
                .then()
                .log().all()
                .apply(document("path/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                authorizationHeader(true)
                        ),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("time").optional().description("현재 시각(yyyyMMddHHmm)"),
                                parameterWithName("type").description("최단 거리 산정 기준")
                        ),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경유역"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
                        )
                ))
                .extract();


    }
}
