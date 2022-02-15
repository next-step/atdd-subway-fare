package nextstep.subway.applicaion.step;

import nextstep.subway.applicaion.dto.LineRequest;

public class LineServiceSteps {

    public static LineRequest 노선_추가_요청_생성(String name, String color,
                                          long upStationId, long downStationId,
                                          int distance, int duration) {
        return LineRequest.of(name, color,
                upStationId, downStationId,
                distance, duration);
    }

}
