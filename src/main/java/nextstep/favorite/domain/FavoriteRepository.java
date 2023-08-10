package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findFavoritesByMemberId(Long memberId);

    default Favorite findFavoriteById(Long id) {
        return findById(id).orElseThrow(RuntimeException::new);
    }

}
