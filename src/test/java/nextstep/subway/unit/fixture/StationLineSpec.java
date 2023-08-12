package nextstep.subway.unit.fixture;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;

import java.math.BigDecimal;
import java.util.UUID;

public class StationLineSpec {
    private StationLineSpec() {
    }

    public static StationLine of(Station upStation, Station downStation, BigDecimal distance, Long duration, BigDecimal additionalFee) {
        return StationLine.builder()
                .name("테스트 노선" + UUID.randomUUID())
                .color("blue")
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .additionalFee(additionalFee)
                .build();
    }
}
