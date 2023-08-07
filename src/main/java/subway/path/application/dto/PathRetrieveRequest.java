package subway.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.auth.principal.UserPrincipal;
import subway.path.domain.PathRetrieveType;

@Getter
@Builder
public class PathRetrieveRequest {
    private long source;
    private long target;
    private PathRetrieveType type;
    private UserPrincipal principal;
}
