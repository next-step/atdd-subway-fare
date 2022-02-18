package nextstep.subway.applicaion.step;

import nextstep.subway.applicaion.dto.LineRequest;

import java.math.BigDecimal;

public class LineServiceSteps {

    public static LineRequest 노선_추가_요청_생성(String name, String color,
                                          long upStationId, long downStationId,
                                          int distance, int duration,
                                          BigDecimal fare) {
        return LineRequest.of(name, color,
                upStationId, downStationId,
                distance, duration,
                fare);
    }

}
