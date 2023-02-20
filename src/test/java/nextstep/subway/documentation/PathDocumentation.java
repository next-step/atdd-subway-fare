package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
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

    @MockBean
    private PathService pathService;

    @Test
    void pathTest() {

        final PathResponse pathResponse = new PathResponse(Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")), 10, 20);

        when(pathService.findPath(anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        given(this.spec)
                .filter(
                        document("path",
                                getPathRequestParametersSnippet(),
                                getPathResponseFieldsSnippet()
                        )
                )
                .when().get("/paths?source={source}&target={target}&type={type}", 1L, 2L, "DISTANCE").then().log().all()
        .assertThat().statusCode(is(HttpStatus.OK.value()));
    }
}
