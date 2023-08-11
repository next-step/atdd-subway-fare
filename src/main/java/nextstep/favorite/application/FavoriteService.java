package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.exception.AuthenticationException;
import nextstep.favorite.application.request.FavoriteCreateRequest;
import nextstep.favorite.application.response.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.SubwayMap;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final LineRepository lineRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, MemberRepository memberRepository, LineRepository lineRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.lineRepository = lineRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorite(UserPrincipal userPrincipal, FavoriteCreateRequest favoriteCreateRequest) {
        Station source = stationRepository.findStation(favoriteCreateRequest.getSource());
        Station target = stationRepository.findStation(favoriteCreateRequest.getTarget());
        Member member = memberRepository.findMemberByEmail(userPrincipal.getEmail());
        SubwayMap subwayMap = new SubwayMap(lineRepository.findAll());

        Favorite favorite = new Favorite(member.getId(), source, target, subwayMap);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(UserPrincipal userPrincipal) {
        Member member = memberRepository.findMemberByEmail(userPrincipal.getEmail());
        return favoriteRepository.findFavoritesByMemberId(member.getId())
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, UserPrincipal userPrincipal) {
        Favorite favorite = favoriteRepository.findFavoriteById(id);
        Member member = memberRepository.findMemberByEmail(userPrincipal.getEmail());
        if (!favorite.isOwner(member)) {
            throw new AuthenticationException();
        }
        favoriteRepository.delete(favorite);
    }

}
