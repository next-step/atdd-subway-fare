package nextstep.member.domain;

import java.util.List;

public interface User {

    User GUEST = new Guest();

    Long getId();

    List<String> getRoles();

    boolean isGuest();

}
