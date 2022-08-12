package nextstep.subway.applicaion.dto;

public class LineSectionRequest {

    private LineRequest lineRequest;
    private SectionRequest sectionRequest;

    private LineSectionRequest() { }

    private LineSectionRequest(LineRequest lineRequest) {
        this(lineRequest, null);
    }

    private LineSectionRequest(LineRequest lineRequest, SectionRequest sectionRequest) {
        this.lineRequest = lineRequest;
        this.sectionRequest = sectionRequest;
    }

    public static LineSectionRequest from(LineRequest lineRequest) {
        return new LineSectionRequest(lineRequest);
    }

    public static LineSectionRequest of(LineRequest lineRequest, SectionRequest sectionRequest) {
        return new LineSectionRequest(lineRequest, sectionRequest);
    }

    public LineRequest getLineRequest() {
        return lineRequest;
    }

    public SectionRequest getSectionRequest() {
        return sectionRequest;
    }
}
