package nextstep.favorite.application;

import nextstep.auth.domain.Account;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.domain.path.PathService;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request, Account account) {
        Member member = memberService.findMemberByEmail(account.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원의 요청입니다."));

        Station sourceStation = stationService.findByStationId(request.getSource());
        Station targetStation = stationService.findByStationId(request.getTarget());
        pathService.findPath(sourceStation.getId(), targetStation.getId(), "DISTANCE", account);

        favoriteRepository.findAllByMember(member).stream()
                .filter(favorite -> favorite.isSamePath(sourceStation, targetStation))
                .findAny()
                .ifPresent(e -> {
                    throw new IllegalArgumentException("이미 등록된 즐겨찾기는 다시 등록할 수 없습니다.");
                });

        Favorite save = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return FavoriteResponse.of(save);
    }

    public List<FavoriteResponse> findFavorites(Account account) {
        Member member = memberService.findMemberByEmail(account.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원의 요청입니다."));
        return favoriteRepository.findAllByMember(member).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, Account account) {
        favoriteRepository.findById(id).ifPresent(favorite -> {
            if (favorite.isNotRegister(account)) {
                throw new AuthenticationException();
            }
        });

        favoriteRepository.deleteById(id);
    }
}
