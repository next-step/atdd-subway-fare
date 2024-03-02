package nextstep.subway.dto.station;

public class StationResponse {
    private Long id;
    private String name;

    protected StationResponse() {}

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
