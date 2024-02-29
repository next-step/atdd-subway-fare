package nextstep.fixture;

import java.util.Random;

import nextstep.api.member.application.dto.MemberRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */

public class MemberFixtureCreator {

	public static MemberRequest createMemberRequest(String email, String password, int age) {
		return new MemberRequest(email, password, age);
	}

	public static int createYouthUserRandomAge() {
		return 13 +  new Random().nextInt(6);
	}

	public static int createChildUserRandomAge() {
		return 6 +  new Random().nextInt(7);
	}
}
