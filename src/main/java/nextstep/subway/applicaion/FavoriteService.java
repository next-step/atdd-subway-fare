package nextstep.subway.applicaion;

import nextstep.common.exception.MissingTokenException;
import nextstep.common.exception.NotFoundException;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.common.error.MemberError.NOT_MISSING_TOKEN;
import static nextstep.common.error.SubwayError.NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final LoginMember loginUser, final FavoriteRequest request) {
        validateLogin(loginUser);
        final Station source = stationService.findById(request.getSource());
        final Station target = stationService.findById(request.getTarget());
        final Favorite saveFavorite = favoriteRepository.save(Favorite.of(loginUser.getMemberId(), source.getId(), target.getId()));
        return FavoriteResponse.of(saveFavorite, StationResponse.from(source), StationResponse.from(target));
    }

    public FavoriteResponse showFavorite(final LoginMember loginUser, final Long id) {
        validateLogin(loginUser);
        final Favorite favorite = favoriteRepository.findByIdAndMemberId(id, loginUser.getMemberId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        final Station source = stationService.findById(favorite.getSourceStationId());
        final Station target = stationService.findById(favorite.getTargetStationId());
        return FavoriteResponse.of(favorite, StationResponse.from(source), StationResponse.from(target));
    }

    @Transactional
    public void removeFavorite(final LoginMember loginUser, final Long id) {
        validateLogin(loginUser);
        favoriteRepository.deleteByIdAndMemberId(id, loginUser.getMemberId());
    }

    private void validateLogin(final LoginMember loginUser) {
        if (loginUser.isGuest()) {
            throw new MissingTokenException(NOT_MISSING_TOKEN);
        }
    }
}
