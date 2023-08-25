package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(updatable = false)
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public Station() { }
    public Station(String name) {
        this.name = name;
    }

    @PrePersist
    private void initializeTime() {
        createdTime = LocalDateTime.now();
        modifiedTime = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdateTime() {
        modifiedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getModifiedTime() { return modifiedTime; }
}