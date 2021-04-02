package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;

import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private PathResponse 강남_역삼_경로;
    private TokenResponse token;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 18;

    @BeforeEach
    void setUp() {
        강남_역삼_경로 = new PathResponse(
                Lists.newArrayList(new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10, 1250
        );
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    void path() {
        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(강남_역삼_경로);

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(getSpec(spec, "path",
                getRequestParametersSnippet(), getResponseFieldsSnippet()), token, 1L, 2L);
    }

    private RequestSpecification getSpec(RequestSpecification spec, String identifier,
                                               RequestParametersSnippet request, ResponseFieldsSnippet response) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        getRequestParametersSnippet(), getResponseFieldsSnippet(),
                        request, response));
    }

    private RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역"),
                parameterWithName("target").description("도착역"),
                parameterWithName("type").description("타입"));
    }

    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description("지하철 역 들"),
                fieldWithPath("stations.[].id").description("지하철 역 아이디"),
                fieldWithPath("stations.[].name").description("지하철 역 이름"),
                fieldWithPath("stations.[].createdDate").description("지하철 역 생성시간"),
                fieldWithPath("stations.[].modifiedDate").description("지하철 역 수정시간"),
                fieldWithPath("distance").description("거리"),
                fieldWithPath("duration").description("(걸린)시간"),
                fieldWithPath("fare").description("요금")
        );
    }
}
