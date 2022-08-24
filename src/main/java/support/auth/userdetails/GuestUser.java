package support.auth.userdetails;

public class GuestUser extends User {
    private GuestUser() {
        super(null, null, null);
    }

    public static GuestUser create() {
        return new GuestUser();
    }
}
