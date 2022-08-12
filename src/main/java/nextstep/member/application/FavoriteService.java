package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.station.application.StationService;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final EntityManager em;

    public FavoriteService(MemberService memberService, StationService stationService, EntityManager em) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.em = em;
    }

    public Long createFavorite(String email, FavoriteRequest request) {
        Member member = memberService.findMember(email);
        Favorite favorite = request.toEntity();

        member.addFavorite(favorite);

        em.flush();
        em.clear();
        return favorite.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(String email) {
        Member member = memberService.findMember(email);
        List<Favorite> favorites = member.getFavorites();

        Map<Long, Station> idToStation = createIdToStationMap(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(idToStation.get(it.getSourceStationId())),
                        StationResponse.of(idToStation.get(it.getTargetStationId()))))
                .collect(toList());
    }

    private Map<Long, Station> createIdToStationMap(List<Favorite> favorites) {
        List<Long> stationIds = new ArrayList<>(extractStationIds(favorites));
        List<Station> stations = stationService.findAllStationsById(stationIds);

        return stations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }

    public void deleteFavorite(String email, Long id) {
        Member member = memberService.findMember(email);
        member.deleteFavorite(id);
    }
}
