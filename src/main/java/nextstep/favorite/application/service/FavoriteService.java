package nextstep.favorite.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.common.error.exception.BadRequestException;
import nextstep.common.error.exception.NotFoundException;
import nextstep.common.error.exception.UnAuthorizedException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetailCustom;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.application.service.StationService;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.SubwayMap;
import nextstep.subway.domain.repository.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final StationService stationService;
    private final MemberRepository memberRepository;
    private final LineRepository lineRepository;
    private final FavoriteRepository favoriteRepository;


    @Transactional
    public Long createFavorite(MemberDetailCustom memberDetailCustom, FavoriteRequest request) {
        Station source = stationService.getStationById(request.getSource());
        Station target = stationService.getStationById(request.getTarget());
        Member member = getMember(memberDetailCustom);
        SubwayMap subwayMap = new SubwayMap(lineRepository.findAll());
        if (!subwayMap.isValidPath(source, target)) {
            throw new BadRequestException("invalid favorite info");
        }
        Favorite favorite = favoriteRepository.save(Favorite.of(member.getId(), source, target));
        return favorite.getId();
    }


    public List<FavoriteResponse> findFavorites(MemberDetailCustom memberDetailCustom) {
        Member member = getMember(memberDetailCustom);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream()
            .map(FavoriteResponse::new)
            .collect(Collectors.toList());
    }


    @Transactional
    public void deleteFavorite(MemberDetailCustom memberDetailCustom, Long id) {
        Member member = getMember(memberDetailCustom);
        Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not found favorite"));
        if (!favorite.isOwner(member.getId())) {
            throw new UnAuthorizedException();
        }
        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }

    private Member getMember(MemberDetailCustom memberDetailCustom) {
        return memberRepository.findByEmail(memberDetailCustom.getId())
            .orElseThrow(() -> new NotFoundException("not found member"));
    }
}
