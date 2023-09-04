package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class LineNotFoundException extends SubwayException {

    public LineNotFoundException() {
        super(SubwayErrorCode.LINE_NOT_FOUND);
    }
}
