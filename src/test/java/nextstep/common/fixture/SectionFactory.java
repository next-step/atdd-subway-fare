package nextstep.common.fixture;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import nextstep.utils.ReflectionUtils;

public class SectionFactory {
    private SectionFactory() {

    }

    public static Section createSection(final Station upStation, final Station downStation, final int distance, final int duration) {
        return new Section(upStation, downStation, distance, duration);
    }

    public static Section createSection(final Long id, final Station upStation, final Station downStation, final int distance, final int duration) {
        final Section section = createSection(upStation, downStation, distance, duration);
        ReflectionUtils.injectIdField(section, id);
        return section;
    }

}
