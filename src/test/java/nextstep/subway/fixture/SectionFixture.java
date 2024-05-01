package nextstep.subway.fixture;

import java.util.Map;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import org.springframework.test.util.ReflectionTestUtils;

public class SectionFixture {

    public static Map<String, Object> 구간_등록_요청_본문(Long upStationId, Long downStationId, Long distance, long duration) {
        return Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance,
            "duration", duration
        );
    }

    public static Section giveOne(Line line, Station upStation, Station downStation, long distance, long duration) {
        return new Section(line, upStation, downStation, distance, duration);
    }

    public static Section giveOne(long id, Line line, Station upStation, Station downStation, long distance, long duration) {
        Section section = new Section(line, upStation, downStation, distance, duration);
        ReflectionTestUtils.setField(section, "id", id);
        return section;
    }
}


