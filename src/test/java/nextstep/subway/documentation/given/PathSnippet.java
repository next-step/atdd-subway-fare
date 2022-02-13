package nextstep.subway.documentation.given;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.snippet.Snippet;

public enum PathSnippet implements SnippetTemplate {
    PATH(
        requestParameters(
            parameterWithName("source").description("출발 역"),
            parameterWithName("target").description("도착 역")
        ),
        responseFields(
            fieldWithPath("stations[].id").description("지하철 역 ID"),
            fieldWithPath("stations[].name").description("지하철 역 이름"),
            fieldWithPath("stations[].createdDate").description("지하철 역 생성 일자").type("Date"),
            fieldWithPath("stations[].modifiedDate").description("지하철 역 수정 일자").type("Date"),
            fieldWithPath("distance").description("총 거리")
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
}
