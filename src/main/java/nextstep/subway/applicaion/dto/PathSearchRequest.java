package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.SectionCondition;

public class PathSearchRequest {
    private Long source;
    private Long target;
    private SectionCondition type;

    public PathSearchRequest() {
    }

    public PathSearchRequest(Long source, Long target, SectionCondition type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public SectionCondition getType() {
        return type;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public void setType(SectionCondition type) {
        this.type = type;
    }
}
