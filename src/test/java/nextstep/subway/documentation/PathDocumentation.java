package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    public static final int FARE = 1350;
    public static final int DURATION = 12;
    public static final int DISTANCE = 10;
    public static final long ID = 1L;
    public static final long ID1 = 2L;
    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(ID, 강남역),
                new StationResponse(ID1, 역삼역)
            ), DISTANCE, DURATION, FARE
        );
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("출발역 id"),
                    parameterWithName("target").description("도착역 id"),
                    parameterWithName("type").description("경로 조회 타입 (DURATION, DISTANCE)")),
                responseFields(
                    fieldWithPath("stations[].id").description("조회 경로 역들의 id"),
                    fieldWithPath("stations[].name").description("조회 경로 역들의 이름"),
                    fieldWithPath("distance").description("조회 경로 거리"),
                    fieldWithPath("duration").description("조회 경로 시간"),
                    fieldWithPath("fare").description("조회 경로 금액")
                )
            ))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", ID)
            .queryParam("target", ID1)
            .queryParam("type", "DURATION")
            .when().get("/paths")
            .then().log().all().extract();

    }
}
