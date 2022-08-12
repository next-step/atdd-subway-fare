package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private int fare;

    private LineRequest() { }

    private LineRequest(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public static LineRequest of(String name, String color, int fare) {
        return new LineRequest(name, color, fare);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getFare() {
        return fare;
    }
}
