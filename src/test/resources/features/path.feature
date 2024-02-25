Feature: 지하철 경로 조회 기능
  Scenario: 교대역에서 양재역까지의 최소 시간 경로 조회
    Given 지하철역이 등록되어있음
      | name       |
      | 교대역        |
      | 강남역        |
      | 양재역        |
      | 남부터미널역    |
    And 지하철 노선이 등록되어있음
      | line | upStationId | downStationId | distance | duration |
      | 2호선  | 1           | 2             | 10       | 10       |
      | 3호선  | 1           | 3             | 5        | 5        |
      | 신분당선 | 2           | 3             | 10       | 20       |
    And 지하철 노선에 지하철역이 등록되어있음
      | lineId | upStationId | downStationId | distance | duration |
      | 2      | 1           | 4             | 2        | 2        |
    When 역ID 1에서 역ID 3까지의 최소 시간 경로를 조회하면
    Then 최소 시간 기준 경로를 응답
      | stationNames     |
      | 교대역, 남부터미널역, 양재역 |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 5        | 5        |

  Scenario: 서울역에서 공덕역까지의 최소 시간 경로 조회
    Given 지하철역이 등록되어있음
      | name             |
      | 서울역              |
      | 시청역              |
      | 홍대입구역            |
      | 이대역              |
      | 디지털미디어시티역      |
      | 공덕역              |
    And 지하철 노선이 등록되어있음
      | line  | upStationId | downStationId | distance | duration |
      | 1호선   | 1           | 2             | 10       | 10       |
      | 2호선   | 2           | 4             | 15       | 15       |
      | 경의중앙선 | 1           | 3             | 5        | 5        |
      | 공항철도  | 3           | 6             | 5        | 5        |
    And 지하철 노선에 지하철역이 등록되어있음
      | lineId | upStationId | downStationId | distance | duration |
      | 2      | 4           | 6             | 20       | 20       |
      | 3      | 3           | 4             | 20       | 20       |
      | 4      | 3           | 5             | 2        | 2        |
    When 역ID 1에서 역ID 6까지의 최소 시간 경로를 조회하면
    Then 최소 시간 기준 경로를 응답
      | stationNames                     |
      | 서울역, 홍대입구역, 디지털미디어시티역, 공덕역 |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 10       | 10       |
