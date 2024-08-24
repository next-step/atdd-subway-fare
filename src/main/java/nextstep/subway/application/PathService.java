package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.LoginMember;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinderService;
import nextstep.subway.domain.PathType;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {
    private final PathFinderService pathFinderService;
    private final UserDetailService userDetailService;

    public PathResponse getPath(Long sourceId, Long targetId, String type, LoginMember loginMember) {
        PathType pathType = PathType.valueOf(type.toUpperCase());

        int age = userDetailService.getUserAge(loginMember);

        Path path = pathFinderService.findPath(sourceId, targetId, pathType, age);
        return PathResponse.of(path, pathType);
    }
}
