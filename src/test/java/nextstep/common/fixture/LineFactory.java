package nextstep.common.fixture;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.utils.ReflectionUtils;

public class LineFactory {
    private LineFactory() {

    }

    public static Line createLine(final String name, final String color, final long extraFare, final Section section) {
        return new Line(name, color, extraFare, section);
    }

    public static Line createLine(final Long id, final String name, final String color, final long extraFare, final Section section) {
        final Line line = createLine(name, color, extraFare, section);
        ReflectionUtils.injectIdField(line, id);
        return line;
    }

}
