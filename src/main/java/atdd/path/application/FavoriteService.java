package atdd.path.application;

import atdd.path.application.dto.CreateStationRequestView;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import atdd.path.repository.FavoriteRepository;
import atdd.path.repository.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public ResponseEntity addStationFavorite(CreateStationRequestView view, User user) {
        Long userId = user.getId();

        Optional<Favorite> favoriteOptional = favoriteRepository.findByUserId(userId);
        Optional<Station> stationOptional = stationRepository.findByName(view.getName());

        Optional<Favorite> optionalFavoriteToSaveStation = stationOptional.map(station -> favoriteOptional
                .map(favorite -> saveStationForFavorite(favorite, station))
                .orElseGet(() -> createFavoriteForStation(user, station)));

        return ResponseEntity.of(optionalFavoriteToSaveStation);
    }

    public ResponseEntity retriveStationFavorites(User user) {
        Long userId = user.getId();

        Optional<Favorite> favoriteOptional = favoriteRepository.findByUserId(userId);

        return ResponseEntity.of(favoriteOptional.map(FavoriteResponseView::of));
    }

    private Favorite saveStationForFavorite(Favorite favorite, Station station) {
        List<Station> stations = favorite.getStations();

        stations.add(station);

        favoriteRepository.save(favorite);

        return favorite;
    }

    private Favorite createFavoriteForStation(User user, Station station) {
        List<Station> newStations = new ArrayList<Station>();
        newStations.add(station);

        Favorite newFavorite = new Favorite(user, newStations);
        favoriteRepository.save(newFavorite);

        return newFavorite;
    }
}
