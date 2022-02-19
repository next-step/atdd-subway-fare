package nextstep.subway.path.documentation;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentationUtils {

    private PathDocumentationUtils() {
    }

    public static PathResponse getPathResponse() {
        return new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10, 10, 2250
        );
    }

    public static RequestParametersSnippet getRequestParameterSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역 고유번호"),
                parameterWithName("target").description("도착역 고유번호"),
                parameterWithName("type").description("DISTANCE: 최단 거리, DURATION: 최소 시간")
        );
    }

    public static ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description("역 목록"),
                fieldWithPath("stations[].id").description("역 고유번호"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("stations[].createdDate").description("생성 시간"),
                fieldWithPath("stations[].modifiedDate").description("최근 수정 시간"),
                fieldWithPath("distance").description("전체 거리"),
                fieldWithPath("duration").description("소요 시간"),
                fieldWithPath("fare").description("요금")
        );
    }
}
