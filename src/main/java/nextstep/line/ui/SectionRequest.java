package nextstep.line.ui;


public class SectionRequest {

    private long downStationId;
    private long upStationId;
    private int distance;
    private int duration;

    protected SectionRequest() {}


    public SectionRequest(long upStationId, long downStationId, int distance, int duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
