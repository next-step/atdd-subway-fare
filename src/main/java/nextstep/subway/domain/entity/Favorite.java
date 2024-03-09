package nextstep.subway.domain.entity;

import nextstep.subway.ui.controller.PathType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @Enumerated
    private PathType pathType;
    private Long source;
    private Long target;

    protected Favorite() {
    }

    public Favorite(Long memberId, PathType pathType, Long source, Long target) {
        this.memberId = memberId;
        this.pathType = pathType;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public PathType getPathType() {
        return pathType;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public boolean notMemberEquals(Long memberId) {
        return !memberId.equals(this.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Favorite) {
            Favorite favorite = (Favorite) obj;
            return Objects.equals(id, favorite.getId());
        }

        return false;
    }
}
