package nextstep.subway.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.exception.NotExistedStationException;
import nextstep.subway.exception.SameStationException;

@ControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NotExistedStationException.class)
	public ResponseEntity<String> handleNotExistedStationException(NotExistedStationException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(SameStationException.class)
	public ResponseEntity<String> handleSameStationException(SameStationException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
