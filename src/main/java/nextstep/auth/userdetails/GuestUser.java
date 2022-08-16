package nextstep.auth.userdetails;

public class GuestUser extends User {

    private static class GuestUserHolder {
        private static final GuestUser INSTANCE = new GuestUser();
    }

    private GuestUser() {
        super("GUEST", null, null);
    }

    public static GuestUser getInstance() {
        return GuestUserHolder.INSTANCE;
    }
}
