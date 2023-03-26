package nextstep.member.ui;

import nextstep.member.exception.MemberErrorMessage;

public class AuthenticationException extends RuntimeException {
	public AuthenticationException() {
		super();
	}

	public AuthenticationException(MemberErrorMessage message) {
		super(message.getMessage());
	}
}
