package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;

public class NonLoginUser extends UserPrincipal{

  public static final String NON_USER = "NonUser";

  private NonLoginUser(String username, String role) {
    super(username, role);
  }

  public static NonLoginUser of(){
    return new NonLoginUser(NON_USER, NON_USER);
  }

  @Override
  public String getUsername(){
    throw new AuthenticationException("로그인이 필요합니다.");
  }

  @Override
  public String getRole(){
    throw new AuthenticationException("로그인이 필요합니다.");
  }

  @Override
  public boolean isLogin(){ return false;}
}
