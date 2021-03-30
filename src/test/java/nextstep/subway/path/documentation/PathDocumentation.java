package nextstep.subway.path.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

  @MockBean
  private PathService pathService;
  private PathResponse 강남역_역삼역_경로;


  @Test
  void path() {

    강남역_역삼역_경로 = new PathResponse(
        Lists.newArrayList(
            new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
            new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())),
        10,
        10
    );

    when(pathService.findPath(anyLong(), anyLong(), any()))
        .thenReturn(강남역_역삼역_경로);

    RestAssured
        .given(spec).log().all()
        .filter(
            document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()))
        )
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .queryParam("source", 1L)
        .queryParam("target", 2L)
        .queryParam("type", "DISTANCE")
        .when().get("/paths")
        .then().log().all().extract();
  }
}

