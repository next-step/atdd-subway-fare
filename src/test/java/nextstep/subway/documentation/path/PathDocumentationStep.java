package nextstep.subway.documentation.path;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.snippet.Snippet;

public class PathDocumentationStep {
  public static RequestParametersSnippet pathRequestParameterSnippet(){
    return requestParameters(
        parameterWithName("source").description("출발역 id"),
        parameterWithName("target").description("도착역 id"),
        parameterWithName("type").description("경로 조회 타입 (DURATION, DISTANCE)"));
  }

  public static ResponseFieldsSnippet pathResponseFieldsSnippet(){
    return responseFields(
        fieldWithPath("stations[].id").description("조회 경로 역들의 id"),
        fieldWithPath("stations[].name").description("조회 경로 역들의 이름"),
        fieldWithPath("distance").description("조회 경로 거리"),
        fieldWithPath("duration").description("조회 경로 시간"),
        fieldWithPath("fare").description("조회 경로 금액")
    );
  }
  public static ResponseFieldsSnippet pathErrorResponseFieldsSnippet(){
    return responseFields(
        fieldWithPath("message").description("에러메시지")
    );
  }
  public static Snippet authorizationHeaderSnippet(){
    return requestHeaders(headerWithName("Authorization").description("액세스 토큰"));
  }
}
