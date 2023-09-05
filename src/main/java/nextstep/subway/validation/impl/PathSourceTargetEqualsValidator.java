package nextstep.subway.validation.impl;

import nextstep.subway.exception.impl.CannotFindPath;
import nextstep.subway.validation.PathValidator;
import nextstep.subway.dto.PathRequest;

public class PathSourceTargetEqualsValidator extends PathValidator {

    public PathSourceTargetEqualsValidator(PathValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public void validate(PathRequest request) {
        if (request.getSource().equals(request.getTarget())) {
            throw new CannotFindPath();
        }

        super.validate(request);
    }
}
