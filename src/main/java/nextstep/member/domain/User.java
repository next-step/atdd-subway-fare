package nextstep.member.domain;

import java.util.List;

public interface User {

    Long getId();

    List<String> getRoles();

    boolean isGuest();

}
