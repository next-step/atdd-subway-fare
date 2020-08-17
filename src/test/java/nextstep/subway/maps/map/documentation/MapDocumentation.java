package nextstep.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import nextstep.subway.Documentation;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.line.dto.LineStationResponse;
import nextstep.subway.maps.map.application.FareMapService;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.maps.map.ui.MapController;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;
import nextstep.subway.utils.TestObjectUtils;

@WebMvcTest(controllers = {MapController.class})
public class MapDocumentation extends Documentation {

    @Autowired
    private MapController mapController;

    @MockBean
    private FareMapService mapService;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
    }

    @Test
    void findMap() {
        Station station1 = TestObjectUtils.createStation(1L, "강남역");
        Station station2 = TestObjectUtils.createStation(2L, "교대역");

        LineStationResponse lineStation1 = new LineStationResponse(StationResponse.of(station1), station2.getId(), 1L,
            3, 3);
        LineStationResponse lineStation2 = new LineStationResponse(StationResponse.of(station2), null, 1L, 0, 0);
        LineResponse lineResponse = new LineResponse(1L, "신분당선", "RED", LocalTime.of(5, 30), LocalTime.of(22, 30), 3,
            Lists.newArrayList(lineStation1, lineStation2), LocalDateTime.now(), LocalDateTime.now(), 1000);
        when(mapService.findSubwayMap())
            .thenReturn(new MapResponse(Lists.newArrayList(lineResponse)));

        given().log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/maps").
            then().
            log().all().
            apply(document("maps/find",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                    fieldWithPath("lineResponses").type(JsonFieldType.ARRAY).description("노선도"),
                    fieldWithPath("lineResponses[].id").type(JsonFieldType.NUMBER).description("노선 아이디"),
                    fieldWithPath("lineResponses[].name").type(JsonFieldType.STRING).description("노선 이름"),
                    fieldWithPath("lineResponses[].color").type(JsonFieldType.STRING).description("노선 색"),
                    fieldWithPath("lineResponses[].startTime").type(JsonFieldType.STRING).description("노선 첫차 배차시간"),
                    fieldWithPath("lineResponses[].endTime").type(JsonFieldType.STRING).description("노선 마지막 배차시간"),
                    fieldWithPath("lineResponses[].intervalTime").type(JsonFieldType.NUMBER).description("배차 간격"),
                    fieldWithPath("lineResponses[].createdDate").type(JsonFieldType.STRING).description("생성일자"),
                    fieldWithPath("lineResponses[].modifiedDate").type(JsonFieldType.STRING).description("수정일자"),
                    fieldWithPath("lineResponses[].extraFare").type(JsonFieldType.NUMBER).description("추가요금"),
                    fieldWithPath("lineResponses[].stations").type(JsonFieldType.ARRAY).description("역 정보"),
                    fieldWithPath("lineResponses[].stations[].station").type(JsonFieldType.OBJECT)
                        .description("지하철 역 정보"),
                    fieldWithPath("lineResponses[].stations[].station.id").type(JsonFieldType.NUMBER)
                        .description("지하철 역 아이디"),
                    fieldWithPath("lineResponses[].stations[].station.name").type(JsonFieldType.STRING)
                        .description("지하철 역 이름"),
                    fieldWithPath("lineResponses[].stations[].preStationId").type(JsonFieldType.NUMBER)
                        .optional()
                        .description("이전 역 아이디"),
                    fieldWithPath("lineResponses[].stations[].lineId").type(JsonFieldType.NUMBER).description("노선 아이디"),
                    fieldWithPath("lineResponses[].stations[].distance").type(JsonFieldType.NUMBER).description("거리"),
                    fieldWithPath("lineResponses[].stations[].duration").type(JsonFieldType.NUMBER).description("소요시간")
                )
            )).
            extract();
    }
}
