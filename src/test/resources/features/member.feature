Feature: 사용자 관련 기능
  Scenario: 회원가입을 한다.
    When 회원을 생성하면
    Then 회원이 생성된다
    And 회원이 조회된다
  Scenario: 회원 정보를 수정한다.
    Given 회원을 생성하고
    When 회원 정보를 수정하면
    Then 회원 정보가 수정된다
  Scenario: 내 정보를 조회한다.
    Given 회원을 생성하고
    And 로그인을 하고
    When 토큰을 통해 내 정보를 조회하면
    Then 내 정보가 조회된다