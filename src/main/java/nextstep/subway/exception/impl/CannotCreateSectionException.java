package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class CannotCreateSectionException extends SubwayException {

    public CannotCreateSectionException() {
        super(SubwayErrorCode.CANNOT_CREATE_SECTION);
    }
}
