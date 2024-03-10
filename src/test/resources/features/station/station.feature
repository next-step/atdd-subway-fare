Feature: 지하철역 관련 기능
  Scenario: 지하철역을 생성한다.
    When 지하철역을 생성하면
    Then 지하철역이 생성된다
    And 지하철역 목록 조회 시 생성한 역을 찾을 수 있다