package nextstep.subway.fixtures;

import nextstep.subway.domain.Station;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class StationFixtures {

    public static final Long 강남역_ID = 1L;

    public static final Long 역삼역_ID = 2L;

    public static final Long 삼성역_ID = 3L;


    public static Station 강남역() {
        return new Station("강남역");
    }

    public static Station 역삼역() {
        return new Station("역삼역");
    }

    public static Station 삼성역() {
        return new Station("삼성역");
    }

    public static Station withId(Station station, Long id) {
        Field idField = ReflectionUtils.findField(station.getClass(), "id");
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, station, id);
        return station;
    }
}
