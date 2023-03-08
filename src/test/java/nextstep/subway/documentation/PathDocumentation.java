package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;

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

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberService memberService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 20, 10
        );

        MemberResponse memberResponse = new MemberResponse(0L, "admin@gmail.com", 6);

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);
        when(jwtTokenProvider.getPrincipal(any())).thenReturn("1");
        when(jwtTokenProvider.getRoles(any())).thenReturn(new ArrayList<>());
        when(memberService.findMember(any())).thenReturn(memberResponse);

        RestAssured
                .given(spec).log().all()
                .header("Authorization", "bearer <access_token>")
                .filter(document("path",
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("type").description("조회 기준")),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 아이디"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간(분)"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리(km)"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금"))))

                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
