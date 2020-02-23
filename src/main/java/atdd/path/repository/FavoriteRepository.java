package atdd.path.repository;

import atdd.path.domain.Favorite;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    Optional<Favorite> findByUserId(Long userId);
}
