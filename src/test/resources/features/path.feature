Feature: 지하철 경로 검색
  Scenario: 두 역의 최단 거리 경로를 조회한다.
    Given 지하철 역들을 생성 요청하고
      | name     |
      | 교대역     |
      | 강남역     |
      | 양재역     |
      | 남부터미널역 |
    Given 지하철 노선들을 생성 요청하고
      | name   | color     | upStationName | downStationName | distance |
      | 2호선   | bg-red    |     교대역      |      강남역       |    10    |
      | 3호선   | bg-green  |     강남역      |      양재역       |    10    |
      | 신분당선 | bg-yellow |     교대역      |      남부터미널역   |     2    |
    Given 지하철 노선에 지하철 구간 생성 요청하고
      | name   | upStationName | downStationName | distance |
      | 3호선   |    남부터미널역   |      양재역       |     3    |
    When "교대역"과 "양재역"의 최단 거리 경로를 조회하면
    Then 최단 거리 경로를 응답 받는다
      | 교대역 |
      | 남부터미널역 |
      | 양재역 |
