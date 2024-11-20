package nextstep.auth.domain;

public class GuestMember implements Account {
    public GuestMember() {
    }

    @Override
    public String getEmail() {
        return "";
    }
}
