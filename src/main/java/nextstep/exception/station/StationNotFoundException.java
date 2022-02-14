package nextstep.exception.station;


import nextstep.exception.ServiceException;

public class StationNotFoundException extends ServiceException {

    private static final String MESSAGE = "역을 찾을 수 없습니다. - %s";

    public StationNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

}
