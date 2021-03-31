package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.application.FareService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.FareRequest;
import nextstep.subway.path.dto.FareResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathSteps.경로_응답됨;
import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


public class PathDocumentation extends Documentation {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    @MockBean
    private FareService fareService;

    @Test
    void path() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, 20);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        FareResponse fareResponse = new FareResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10,
                10,
                2000
        );
        when(fareService.calculate(any(FareRequest.class))).thenReturn(fareResponse);

        // when
        ExtractableResponse< Response > response =  두_역의_최단_거리_경로_조회를_요청(getPathGiven(),tokenResponse, 1L,2L);

        // then
        경로_응답됨(response, com.google.common.collect.Lists.newArrayList(1L, 2L), 10, 10, 2000);
    }

    private RequestSpecification getPathGiven(){
       return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("The id of source station"),
                                parameterWithName("target").description("The id of source destination"),
                                parameterWithName("type").description("The request type of path")
                        ),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("Path of stations"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The id of station"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The name of station"),
                                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("The registered date of station"),
                                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("The updated date of station"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("The distance between path"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("The taken time"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("The money to pay")
                        )));
    }
}
