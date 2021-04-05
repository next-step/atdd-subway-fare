package nextstep.subway.path.domain.policy;

public enum PolicyConstant {

    // 기본요금
    BASIC_FARE(1250)
    // 거리정책
    , D1_MIN_DISTANCE(10)
    , D1_MAX_DISTANCE(50)
    , D1_UNIT_DISTANCE(5)
    , D2_MIN_DISTANCE(50)
    , D2_MAX_DISTANCE(Integer.MAX_VALUE)
    , D2_UNIT_DISTANCE(8);

    private final int value;

    PolicyConstant(int value){
        this.value = value;
    }

    public int of() {
        return value;
    }
}
