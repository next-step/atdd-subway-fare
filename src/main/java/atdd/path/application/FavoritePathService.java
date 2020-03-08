package atdd.path.application;


import atdd.path.dao.FavoritePathDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoritePath;
import atdd.path.domain.Station;
import atdd.user.application.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePathService {
    private FavoritePathDao favoritePathDao;
    private UserDao userDao;
    private StationDao stationDao;

    public FavoritePathService(FavoritePathDao favoritePathDao, UserDao userDao, StationDao stationDao) {
        this.favoritePathDao = favoritePathDao;
        this.userDao = userDao;
        this.stationDao = stationDao;
    }

    public Station findStationById(final long stationId) {
        return stationDao.findById(stationId);
    }

    public FavoritePath addFavoritePath(final FavoritePath favoritePath) {
        return favoritePathDao.save(favoritePath);
    }

    public List<FavoritePath> findFavoritePath(final long userId) {
        return favoritePathDao.findAll(userId);
    }

    public void deleteFavoritePath(final String email, final long id) {
        User user = userDao.findByEmail(email);

        favoritePathDao.deleteById(user.getId(), id);
    }
}
