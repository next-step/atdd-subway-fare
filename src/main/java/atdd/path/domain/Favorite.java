package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Station> stations;

    protected Favorite() {

    }

    public Favorite(User user, List<Station> stations) {
        this.user = user;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Station> getStations() {
        return stations;
    }
}
