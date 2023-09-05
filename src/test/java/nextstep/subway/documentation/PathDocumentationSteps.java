package nextstep.subway.documentation;

import static nextstep.subway.acceptance.utils.SubwayClient.구간_생성_요청;
import static nextstep.subway.acceptance.utils.SubwayClient.노선_생성_요청;
import static nextstep.subway.acceptance.utils.SubwayClient.지하철역_생성_요청;

import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.dto.StationRequest;
import nextstep.subway.dto.StationResponse;

public class PathDocumentationSteps {

    public static PathInformation 경로_조회_요청_문서_데이터_생성() {
        Long 교대역 = 지하철역_생성_요청(new StationRequest("교대역")).as(StationResponse.class).getId();
        Long 양재역 = 지하철역_생성_요청(new StationRequest("양재역")).as(StationResponse.class).getId();
        Long 남부터미널역 = 지하철역_생성_요청(new StationRequest("남부터미널역")).as(StationResponse.class).getId();

        Long 삼호선 = 노선_생성_요청(new LineRequest("삼호선", "bg-orange-600", 교대역, 양재역, 5L))
            .as(LineResponse.class).getId();
        구간_생성_요청(삼호선, new SectionRequest(남부터미널역, 양재역, 2L));

        return new PathInformation(교대역, 양재역);
    }

    public static class PathInformation {
        Long 출발역;
        Long 도착역;

        public PathInformation(Long 출발역, Long 도착역) {
            this.출발역 = 출발역;
            this.도착역 = 도착역;
        }
    }
}
