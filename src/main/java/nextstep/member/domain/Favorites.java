package nextstep.member.domain;

import nextstep.member.domain.exception.DuplicateFavoriteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite favorite) {
        favorites.stream()
                .filter(it -> it.isSame(favorite))
                .findAny()
                .ifPresent(it -> {
                    throw new DuplicateFavoriteException();
                });

        favorites.add(favorite);
    }

    public void delete(Long id) {
        favorites.removeIf(it -> it.matchId(id));
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }
}
