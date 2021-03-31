package nextstep.subway.path.dto;

import nextstep.subway.line.domain.PathType;

public class FareRequest {
    private Long source;
    private Long target;
    private PathType type;
    private int age;

    public FareRequest(){}

    public FareRequest(Long source, Long target, PathType type, int age) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.age = age;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public PathType getType() {
        return type;
    }

    public int getAge() {
        return age;
    }
}
