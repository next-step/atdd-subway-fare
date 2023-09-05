package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class NoMatchStationException extends SubwayException {

    public NoMatchStationException() {
        super(SubwayErrorCode.NO_MATCH_UP_STATION);
    }

}
