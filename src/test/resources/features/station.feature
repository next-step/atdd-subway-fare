Feature: 지하철역 관련 기능
  Scenario: 지하철역을 생성한다.
    When 지하철역을 생성하면
    Then 지하철역이 생성된다
    And 지하철역 목록 조회 시 생성한 역을 찾을 수 있다

  Scenario: 지하철역들을 생성하고 경로를 찾는다.
    Given 지하철역들을 생성하고
      |name|
      |교대역 |
      |강남역 |
      |양재역 |
      |남부터미널역|
    Given 지하철 노선들을 생성하고
      |name|color|upStation|downStation|distance|duration|surcharge|
      |3호선  | orange | 교대역 | 남부터미널역    | 2      | 10     | 0   |
    Given '3호선'에 지하철 역을 추가하고
      |upStation|downStation|distance|duration|
      |남부터미널역   |양재역        |3       |10      |
    When '교대역'과 '양재역' 사이 경로를 조회하면
    Then '교대역'-'남부터미널역'-'양재역' 경로가 조회된다
