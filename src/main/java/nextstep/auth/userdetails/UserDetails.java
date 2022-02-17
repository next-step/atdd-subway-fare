package nextstep.auth.userdetails;

public interface UserDetails {
    Long getId();
    String getEmail();
    String getPassword();
    Integer getAge();
    boolean checkCredentials(String credentials);
}
