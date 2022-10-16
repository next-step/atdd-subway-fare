package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.favorite.Favorite;
import nextstep.subway.domain.favorite.FavoriteRepository;
import nextstep.subway.domain.station.Station;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public void createFavorite(String email, FavoriteRequest request) {
        MemberResponse member = memberService.findMember(email);
        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(String email) {
        MemberResponse member = memberService.findMember(email);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(String email, Long id) {
        MemberResponse member = memberService.findMember(email);
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!favorite.isCreatedBy(member.getId())) {
            throw new RuntimeException();
        }
        favoriteRepository.deleteById(id);
    }

    private Map<Long, Station> extractStations(List<Favorite> favorites) {
        Set<Long> stationIds = extractStationIds(favorites);
        return stationService.findAllStationsById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }
}
