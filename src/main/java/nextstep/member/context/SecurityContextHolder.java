package nextstep.member.context;

public class SecurityContextHolder  {
    private static SecurityContext context = new SecurityContext();

    public void setContext(String token) {
        context.setAuthentication(token);
    }

    public static SecurityContext getContext() {
        return context;
    }
}
