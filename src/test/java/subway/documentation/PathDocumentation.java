package subway.documentation;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import subway.path.application.PathService;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.dto.StationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        StationResponse 강남역 = StationResponse.builder()
                .id(1L)
                .name("강남역").build();
        StationResponse 역삼역 = StationResponse.builder()
                .id(2L)
                .name("역삼역").build();
        PathRetrieveResponse pathRetrieve = PathRetrieveResponse.builder()
                .stations(List.of(강남역, 역삼역))
                .distance(10)
                .build();

        when(pathService.getShortestPath(anyLong(), anyLong())).thenReturn(pathRetrieve);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/path")
                .then().log().all().extract();
    }
}
