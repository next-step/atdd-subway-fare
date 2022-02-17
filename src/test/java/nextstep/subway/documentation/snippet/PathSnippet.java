package nextstep.subway.documentation.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.snippet.Snippet;

public enum PathSnippet implements SnippetTemplate {

    PATH(
        Base.COMMON_REQUEST_PARAMS,
        Base.COMMON_RESPONSE_FIELDS
    ),
    PATH_BY_ARRIVAL_TIME(
        Base.COMMON_REQUEST_PARAMS.and(
            parameterWithName("time").description("출발 예정 시각")
        ),
        Base.COMMON_RESPONSE_FIELDS.and(
            fieldWithPath("arrivalTime").description("도착 일시")
        )
    );

    private final Snippet[] snippets;

    PathSnippet(Snippet... snippets) {
        this.snippets = snippets;
    }

    @Override
    public Snippet[] getSnippets() {
        return snippets;
    }
    private static class Base {
        public static final RequestParametersSnippet COMMON_REQUEST_PARAMS = requestParameters(
            parameterWithName("source").description("출발 역"),
            parameterWithName("target").description("도착 역")
        );

        public static final ResponseFieldsSnippet COMMON_RESPONSE_FIELDS = responseFields(
            fieldWithPath("stations[].id").description("지하철 역 ID"),
            fieldWithPath("stations[].name").description("지하철 역 이름"),
            fieldWithPath("stations[].createdDate").description("지하철 역 생성 일자"),
            fieldWithPath("stations[].modifiedDate").description("지하철 역 수정 일자"),
            fieldWithPath("distance").description("총 거리"),
            fieldWithPath("duration").description("총 소요 시간"),
            fieldWithPath("totalCost").description("총 요금")
        );
    }
}
