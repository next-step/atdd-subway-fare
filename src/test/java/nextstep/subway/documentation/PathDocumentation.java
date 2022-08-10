package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Arrays.asList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ),
            10,
            10,
            1250
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);


        RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .filter(document("path/duration",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("stations.[].id")
                            .description("역 식별자"),
                        fieldWithPath("stations.[].name")
                            .description("역 이름"),
                        fieldWithPath("distance")
                            .description("경유하는 거리"),
                        fieldWithPath("duration")
                            .description("최소 소요 시간"),
                        fieldWithPath("fare")
                            .description("요금")
                    )
                )
            )
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .when().get("/paths/duration")
            .then().log().all().extract();
    }
}
