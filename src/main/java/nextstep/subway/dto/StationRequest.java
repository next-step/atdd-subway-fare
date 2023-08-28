package nextstep.subway.dto;

public class StationRequest {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }
}
