package atdd.favorite.application.dto;

public class CreateFavoritePathRequestView {

    private Long startId;
    private Long endId;

    private CreateFavoritePathRequestView() { }

    public Long getStartId() {
        return startId;
    }

    public Long getEndId() {
        return endId;
    }

}
