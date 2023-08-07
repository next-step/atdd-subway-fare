package nextstep.subway.documentation;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.PathSteps.출력_필드_추가;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineDocumentation extends Documentation {

    @DisplayName("노선 생성 문서")
    @Test
    void saveLine() {
        // given
        var 신사역_아이디 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
        var 광교역_아이디 = 지하철역_생성_요청("광교역").jsonPath().getLong("id");
        int 신사역에서_광교역까지의_거리 = 15;
        int 신사역에서_광교역까지_걸리는_시간 = 42 * 60;
        출력_필드_추가("saveLine", spec);

        // when,then
        지하철_노선_생성_요청(
                "신분당선",
                "red",
                신사역_아이디,
                광교역_아이디,
                신사역에서_광교역까지의_거리,
                신사역에서_광교역까지_걸리는_시간,
                spec
        );
    }
}
