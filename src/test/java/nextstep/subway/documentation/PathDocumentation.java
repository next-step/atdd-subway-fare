package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.sectiontype.SectionPathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L,"강남역",null,null),
                        new StationResponse(2L,"역삼역",null,null)
                )      ,10,10
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("경로 시작 ID"),
                                parameterWithName("target").description("경로 종료 ID"),
                                parameterWithName("type").description("경로 조회 타입")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("역 리스트"),
                                fieldWithPath("stations[].id").description("역 아이디"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("stations[].createdDate").description("생성일"),
                                fieldWithPath("stations[].modifiedDate").description("변경일"),
                                fieldWithPath("distance").description("거리"),
                                fieldWithPath("duration").description("시간")
                        )))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", SectionPathType.DISTANCE)
                .when().get("/paths")
                .then().log().all().extract();
    }


}
