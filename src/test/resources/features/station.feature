Feature: 지하철역 관련 기능
  Scenario: 지하철역을 생성한다.
    When 지하철역을 생성하면
    Then 지하철역이 생성된다
    And 지하철역 목록 조회 시 생성한 역을 찾을 수 있다

  Scenario: 두 역 간의 경로를 조회한다.
    Given 지하철역들을 생성하고
      |name|
      |삼전역 |
      |종합운동장역 |
      |봉은사역 |
      |신논현역 |
    Given 지하철 노선을 생성하고
      |name|color|upStation|downStation|distance|
      |9호선  | brown | 삼전역 | 종합운동장역    | 2      |
    Given '9호선'에 지하철역을 추가하고
      |upStation|downStation|distance|
      |종합운동장역  |봉은사역        |5       |
    Given '9호선'에 지하철역을 추가하고
      |upStation|downStation|distance|
      |봉은사역  |신논현역        |5       |
    When '삼전역'과 '봉은사역' 사이 경로를 조회하면
    Then '삼전역'-'종합운동장역'-'봉은사역' 경로가 조회된다.
