package atdd.favorite.application.dto;

public class FavoritePathRequestView {
    private Long id;
    private String email;
    private Long startId;
    private Long endId;

    public FavoritePathRequestView() {
    }

    public FavoritePathRequestView(Long id) {
        this(id, null, null, null);
    }

    public FavoritePathRequestView(String email) {
        this(null, email, null, null);
    }

    public FavoritePathRequestView(Long id, String email) {
        this(id, email, null, null);
    }

    public FavoritePathRequestView(Long startId, Long endId) {
        this(null, null, startId, endId);
    }

    public FavoritePathRequestView(String email, Long startId, Long endId) {
        this(null, email, startId, endId);
    }

    public FavoritePathRequestView(Long id, String email, Long startId, Long endId) {
        this.id = id;
        this.email = email;
        this.startId = startId;
        this.endId = endId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getStartId() {
        return startId;
    }

    public Long getEndId() {
        return endId;
    }

    public void insertId(Long id) {
        this.id = id;
    }

    public void insertEmail(String email) {
        this.email = email;
    }

    public boolean isSameStation() {
        return this.getStartId() == this.getEndId();
    }
}
