Feature: 경로 조회 관련 기능

  Scenario: 두 역의 최단 거리 경로를 조회한다.
    Given 지하철역들을 생성 요청하고
      | name   |
      | 교대역    |
      | 강남역    |
      | 양재역    |
      | 남부터미널역 |
    Given 지하철 노선들을 생성 요청하고
      | name | color  | upStationName | downStationName | distance |
      | 이호선  | green  | 교대역           | 강남역             | 10       |
      | 신분당선 | red    | 강남역           | 양재역             | 10       |
      | 삼호선  | orange | 교대역           | 남부터미널역          | 2        |
    Given "삼호선" 노선에 지하철 "남부터미널역" "양재역" 구간을 거리 3으로 생성 요청하고
    When "교대역"과 "양재역"의 경로를 조회하면
    Then 두 역의 최단 경로를 찾을 수 있다
      | 교대역    |
      | 남부터미널역 |
      | 양재역    |