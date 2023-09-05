package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class CannotFindPath extends SubwayException {

    public CannotFindPath() {
        super(SubwayErrorCode.CANNOT_FIND_PATH);
    }
}
