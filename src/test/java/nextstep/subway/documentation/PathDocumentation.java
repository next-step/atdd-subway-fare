package nextstep.subway.documentation;

import static nextstep.subway.documentation.step.PathDocumentationStep.경로_조회_문서화;
import static nextstep.subway.documentation.step.PathDocumentationStep.경로_조회_응답_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(경로_조회_응답_생성());

        RestAssured
            .given(spec).log().all()
            .filter(경로_조회_문서화())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParam("pathType", PathType.DISTANCE.name())
            .when().get("/paths")
            .then().log().all().extract();
    }
}
