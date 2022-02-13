package nextstep.subway.documentation;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.즐겨찾기_조회하기_문서화_스펙_정의;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
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
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathDocumentation extends Documentation {

    @Test
    void path() {
        spec = 즐겨찾기_조회하기_문서화_스펙_정의(restDocumentation);

        StationResponse 교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);

        Map<String, String> params = new HashMap<>();
        params.put("name", "이호선");
        params.put("color", "이호선");
        LineResponse 이호선 = 지하철_노선_생성_요청(params).as(LineResponse.class);

        params = new HashMap<>();
        params.put("upStationId", 교대역.getId().toString());
        params.put("downStationId", 양재역.getId().toString());
        params.put("distance", "20");
        params.put("duration", "10");
        지하철_노선에_지하철_구간_생성_요청(이호선.getId(), params);

        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역.getId())
            .queryParam("target", 양재역.getId())
            .when().get("/paths/distance")
            .then().assertThat().statusCode(is(200))
            .log().all().extract();

        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getStations().size()).isEqualTo(2);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(10);
    }

}
