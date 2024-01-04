package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public FavoriteResponse createFavorite(UserPrincipal userPrincipal, FavoriteRequest request) {
        Member member = memberService.findMemberByEmail(userPrincipal.getUsername());
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(UserPrincipal userPrincipal) {
        Member member = memberService.findMemberByEmail(userPrincipal.getUsername());
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(UserPrincipal userPrincipal, Long id) {
        Member member = memberService.findMemberByEmail(userPrincipal.getUsername());
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!favorite.isCreatedBy(member)) {
            throw new RuntimeException();
        }
        favoriteRepository.deleteById(id);
    }
}
