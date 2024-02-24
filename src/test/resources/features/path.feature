Feature: 지하철 경로 조회 기능
  Scenario: 교대역에서 양재역까지의 최단 경로 조회
    Given 지하철 역들이 생성되어 있다:
      | name       |
      | 교대역        |
      | 강남역        |
      | 양재역        |
      | 남부터미널역    |
    And 지하철 노선들이 생성되어 있다:
      | line | upStationId | downStationId | distance |
      | 2호선  | 1           | 2             | 10       |
      | 3호선  | 1           | 3             | 5        |
      | 신분당선 | 2           | 3             | 10       |
    And 다음 구간이 추가되어 있다:
      | lineId | upStationId | downStationId | distance |
      | 2      | 1           | 4             | 2        |
    When 역ID 1에서 역ID 3까지의 최단 경로를 조회하면
    Then 최단 경로가 정확하게 반환된다:
      | stationNames          | distance |
      | 교대역, 남부터미널역, 양재역 | 5        |

