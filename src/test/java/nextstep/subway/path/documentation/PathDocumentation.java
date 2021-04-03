package nextstep.subway.path.documentation;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
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
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import nextstep.subway.Documentation;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

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
        10,
        1250
    );

    when(pathService.findPath(anyLong(), anyLong(), any(),new LoginMember(1L,"test@test.com","password",7)))
        .thenReturn(강남역_역삼역_경로);

    RequestParametersSnippet requestParametersSnippet = requestParameters(
        parameterWithName("source").description("출발역 식별ID"),
        parameterWithName("target").description("도착역 식별ID"),
        parameterWithName("type").description("경로 찾기 타입"));

    ResponseFieldsSnippet responseFieldsSnippet = responseFields(
        fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("최단 경로 역 정보"),
        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("최단 경로 역 식별ID"),
        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("최단 경로 역 이름"),
        fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("최단 경로 역 생성시간"),
        fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("최단 경로 역 수정시간"),
        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("최단 경로 거리"),
        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("최단 경로 시간"),
        fieldWithPath("fare").type(JsonFieldType.NUMBER).description("최단 경로 요금")
    );


    RequestSpecification requestSpecification = RestAssured
        .given(spec)
        .filter(document("path",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParametersSnippet,
            responseFieldsSnippet)
        );

    두_역의_최단_거리_경로_조회를_요청(requestSpecification, 1L, 2L,"DISTANCE");
  }
}

