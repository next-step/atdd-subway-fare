package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    public LineRequest() {
    }

    public LineRequest(Builder builder) {
        this.name = builder.name;
        this.color = builder.name;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
        this.duration = builder.duration;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public static class Builder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private int distance;
        private int duration;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public LineRequest build() {
            return new LineRequest(this);
        }

    }

}
