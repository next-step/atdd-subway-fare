package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.PathSteps.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    private static final String 최소시간 = "DURATION";
    @MockBean
    private PathService pathService;

    @DisplayName("두 역의 거리 및 시간을 구한다.")
    @Test
    void pathTest() {

        final StationResponse 강남역 = new StationResponse(1L, "강남역");
        final StationResponse 역삼역 = new StationResponse(2L, "역삼역");
        final PathResponse pathResponse = new PathResponse(Lists.newArrayList(
                강남역, 역삼역), 10, 20);

        when(pathService.findPath(anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        given(this.spec)
                .filter(
                        document("path",
                                getPathRequestParametersSnippet(),
                                getPathResponseFieldsSnippet()
                        )
                )
                .queryParam("source", 강남역.getId())
                .queryParam("target", 역삼역.getId())
                .queryParam("type", 최소시간)
                .when().get("/paths").then().log().all()
        .assertThat().statusCode(is(HttpStatus.OK.value()));
    }
}
