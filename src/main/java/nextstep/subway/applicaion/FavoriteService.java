package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public void createFavorite(Long memberId, FavoriteRequest request) {
        MemberResponse member = memberService.findMember(memberId);
        Favorite favorite = new Favorite(member.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        MemberResponse member = memberService.findMember(memberId);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long id) {
        MemberResponse member = memberService.findMember(memberId);
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
