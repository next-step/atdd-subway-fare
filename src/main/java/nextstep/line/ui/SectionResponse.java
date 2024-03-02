package nextstep.line.ui;

public class SectionResponse {

    private long upStationId;
    private long downStationId;
    private int distance;
    private int duration;

    public SectionResponse(long upStationId, long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    protected SectionResponse() {
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
