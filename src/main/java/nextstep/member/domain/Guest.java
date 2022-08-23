package nextstep.member.domain;


import java.util.Collections;
import java.util.List;

public class Guest extends Member {

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public Integer getAge() {
        return 0;
    }

    @Override
    public List<String> getRoles() {
        return Collections.emptyList();
    }
}
