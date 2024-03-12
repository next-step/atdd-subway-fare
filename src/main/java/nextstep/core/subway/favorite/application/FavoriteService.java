package nextstep.core.subway.favorite.application;

import nextstep.core.member.domain.Member;
import nextstep.core.member.exception.NonMatchingMemberException;
import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.favorite.application.dto.FavoriteResponse;
import nextstep.core.subway.favorite.domain.Favorite;
import nextstep.core.subway.favorite.domain.FavoriteRepository;
import nextstep.core.subway.path.application.PathService;
import nextstep.core.subway.path.application.dto.PathRequest;
import nextstep.core.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final PathService pathService;

    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, PathService pathService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.pathService = pathService;
        this.stationService = stationService;
    }

    @Transactional
    public Favorite createFavorite(FavoriteRequest request, Long memberId) {
        if (!pathService.isValidPath(PathRequest.toRequest(request))) {
            throw new IllegalArgumentException("출발역과 도착역을 잇는 경로가 없습니다.");
        }
        return favoriteRepository.save(createFavoriteEntity(request, memberId));
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        List<Favorite> favorites = favoriteRepository.findByMember(member.getId());
        return FavoriteResponse.toResponse(favorites);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Member member) {
        Favorite favorite = findFavoriteById(favoriteId);
        if (!favorite.isOwn(member)) {
            throw new NonMatchingMemberException("다른 회원의 즐겨찾기를 삭제할 수 없습니다.");
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("즐겨찾기 번호에 해당하는 즐겨찾기가 없습니다."));
    }

    private Favorite createFavoriteEntity(FavoriteRequest request, Long memberId) {
        return new Favorite(
                stationService.findStation(request.getSource()),
                stationService.findStation(request.getTarget()),
                memberId);
    }
}
