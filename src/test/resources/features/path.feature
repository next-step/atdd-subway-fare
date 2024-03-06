Feature: 지하철 경로 관련 기능
  Scenario: 경로가 다건일 경우, 최단 경로 조회 성공
    Given 지하철역들을 생성하고
      | name    |
      | 도곡역   |
      | 학여울역 |
      | 수서역   |
      | 개포동역 |
      | 판교역   |
      | 양재역   |
      | 수원역   |
    And 지하철 노선들을 생성하고
      | name  | color  | startStation | endStation | distance |
      | 분당선 | 노랑    | 도곡역        | 학여울역    | 6       |
      | 삼호선 | 주황    | 도곡역        | 개포동역    | 4       |
    And 지하철 구간들을 생성하고
      | downStation| upStation | distance| lineName |
      | 수서역       | 학여울역    | 10      | 분당선     |
      | 수서역       | 개포동역    | 8       | 삼호선     |
    When "수서역"부터 "도곡역"까지의 경로를 조회하면
    Then 경로에 있는 역 목록은 "수서역,개포동역,도곡역" 순서대로 구성된다
    And 경로의 거리는 "12"이다
