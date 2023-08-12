package nextstep.api.subway.domain.path;

import java.util.Arrays;

import nextstep.api.SubwayException;
import nextstep.api.subway.domain.line.Section;

public enum PathSelection {
    DISTANCE {
        @Override
        public int getValueOf(final Section section) {
            return section.getDistance();
        }
    },
    DURATION {
        @Override
        public int getValueOf(final Section section) {
            return section.getDuration();
        }
    },
    ;

    public static PathSelection from(final String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new SubwayException("지원하지 않는 경로조회 타입입니다"));
    }

    public abstract int getValueOf(final Section section);
}
