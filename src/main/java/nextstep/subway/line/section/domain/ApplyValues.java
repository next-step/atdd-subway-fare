package nextstep.subway.line.section.domain;

public class ApplyValues {
    private final ApplyType applyType;
    private final Long distance;
    private final Long duration;

    public ApplyValues(ApplyType applyType,
                       Long distance,
                       Long duration) {
        this.applyType = applyType;
        this.distance = distance;
        this.duration = duration;
    }

    public static ApplyValues applyAddFirst(Long distance,
                                            Long duration) {
        return new ApplyValues(ApplyType.ADD_FIRST, distance, duration);
    }

    public static ApplyValues applyAddLast(Long distance,
                                           Long duration) {
        return new ApplyValues(ApplyType.ADD_LAST, distance, duration);
    }

    public static ApplyValues applyAddMiddle() {
        return new ApplyValues(ApplyType.ADD_MIDDLE, 0L, 0L);
    }

    public static ApplyValues applyDeleteFirst(Long distance,
                                               Long duration) {
        return new ApplyValues(ApplyType.DELETE_FIRST, distance, duration);
    }

    public static ApplyValues applyDeleteLast(Long distance,
                                              Long duration) {
        return new ApplyValues(ApplyType.DELETE_LAST, distance, duration);
    }

    public static ApplyValues applyDeleteMiddle() {
        return new ApplyValues(ApplyType.DELETE_MIDDLE, 0L, 0L);
    }

    public Long applyDistance() {
        if (this.applyType.isApplyMiddle()) {
            return 0L;
        }
        return this.distance;
    }

    public Long applyDuration() {
        if (this.applyType.isApplyMiddle()) {
            return 0L;
        }
        return this.duration;
    }

    public void validAdd(Long distance,
                         Long addDistance) {
        if (this.applyType.isApplyMiddle() && (distance <= addDistance)) {
            throw new IllegalArgumentException("중간에 추가되는 구간은 라인보다 길수 없습니다.");
        }
    }
}
