package nextstep.subway.documentation.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.snippet.Snippet;

public enum LineSnippet implements SnippetTemplate {
    ADD(
        requestFields(
            fieldWithPath("name").description("노선 이름"),
            fieldWithPath("color").description("노선 색상"),
            fieldWithPath("upStationId").description("상행 ID"),
            fieldWithPath("downStationId").description("하행 ID"),
            fieldWithPath("distance").description("상행과 하행의 거리"),
            fieldWithPath("duration").description("상행과 하행의 소요 시간")
        ),
        responseFields(
            fieldWithPath("id").description("ID"),
            fieldWithPath("name").description("이름"),
            fieldWithPath("color").description("색상"),
            fieldWithPath("createdDate").description("생성 일자"),
            fieldWithPath("modifiedDate").description("수정 일자"),
            fieldWithPath("stations[].id").description("노선 내 지하철역 ID"),
            fieldWithPath("stations[].name").description("노선 내 지하철역 이름"),
            fieldWithPath("stations[].createdDate").description("노선 내 지하철역 생성 일자"),
            fieldWithPath("stations[].modifiedDate").description("노선 내 지하철역 수정 일자")
        )
    );

    private final Snippet[] snippets;

    LineSnippet(Snippet... snippets) {
        this.snippets = snippets;
    }

    @Override
    public Snippet[] getSnippets() {
        return snippets;
    }
}
