package nextstep.subway.exception.impl;

import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.error.SubwayErrorCode;

public class AlreadyExistDownStation extends SubwayException {

    public AlreadyExistDownStation() {
        super(SubwayErrorCode.ALREADY_EXIST_DOWN_STATION);
    }

}
