package nextstep.core.subway.section.fixture;

import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.section.application.dto.SectionRequest;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.fixture.StationFixture;

public class SectionFixture {
    public static Section 강남_양재_구간(int distance, int duration, Line line) {
        return new Section(StationFixture.강남, StationFixture.양재, distance, duration, line);
    }

    public static Section 삼성_선릉_구간(int distance, int duration, Line line) {
        return new Section(StationFixture.삼성, StationFixture.선릉, distance, duration, line);
    }

    public static Section 선릉_역삼_구간(int distance, int duration, Line line) {
        return new Section(StationFixture.선릉, StationFixture.역삼, distance, duration, line);
    }

    public static Section 역삼_강남_구간(int distance, int duration, Line line) {
        return new Section(StationFixture.역삼, StationFixture.강남, distance, duration, line);
    }

    public static Section 강남_서초_구간(int distance, int duration, Line line) {
        return new Section(StationFixture.강남, StationFixture.서초, distance, duration, line);
    }

    public static SectionRequest 지하철_구간(Long upStationId, Long downStationId, int distance, int duration) {
        return new SectionRequest(upStationId, downStationId, distance, duration);
    }

}