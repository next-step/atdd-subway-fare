package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class CannotDeleteSectionException extends SubwayException {

    public CannotDeleteSectionException() {
        super(SubwayErrorCode.CANNOT_DELETE_SECTION);
    }
}
