package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class StationNotFoundException extends SubwayException {

    public StationNotFoundException() {
        super(SubwayErrorCode.STATION_NOT_FOUND);
    }
}
