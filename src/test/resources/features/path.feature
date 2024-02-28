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
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 1750       |

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
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 2450       |


  Scenario: 교대역에서 양재역까지의 최단 거리 경로 조회
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
    When 역ID 1에서 역ID 3까지의 최단 거리 경로를 조회하면
    Then 최단 거리 기준 경로를 응답
      | stationNames     |
      | 교대역, 남부터미널역, 양재역 |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 5        | 5        |
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 1750       |

  Scenario: 서울역에서 공덕역까지의 최단 거리 경로 조회
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
    When 역ID 1에서 역ID 6까지의 최단 거리 경로를 조회하면
    Then 최단 거리 기준 경로를 응답
      | stationNames                     |
      | 서울역, 홍대입구역, 디지털미디어시티역, 공덕역 |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 10       | 10       |
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 2450        |


  Scenario: 다양한 거리에 대한 지하철 이용 요금 계산
    Given 지하철역이 등록되어있음
      | name   |
      | 역A    |
      | 역B    |
      | 역C    |
    And 지하철 노선이 등록되어있음
      | line | upStationId | downStationId | distance | duration |
      | 1호선  | 1           | 2             | 9        | 10       |
      | 2호선  | 2           | 3             | 12       | 15       |
      | 3호선  | 1           | 3             | 16       | 20       |
    When 역ID 1에서 역ID 2까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 1450원이다
    When 역ID 2에서 역ID 3까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 1850원이다
    When 역ID 1에서 역ID 3까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 2350원이다

  Scenario: 10km를 초과하고 50km 이내의 거리에 대한 지하철 이용 요금 계산
    Given 지하철역이 등록되어있음
      | name |
      | 역A   |
      | 역B   |
      | 역C   |
      | 역D   |
      | 역E   |
    And 지하철 노선이 등록되어있음
      | line | upStationId | downStationId | distance | duration |
      | 4호선  | 1           | 2             | 20       | 20       |
      | 5호선  | 2           | 3             | 24       | 30       |
      | 6호선  | 2           | 4             | 29       | 5        |
      | 7호선  | 2           | 5             | 30       | 1        |
    When 역ID 1에서 역ID 2까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 1650원이다
    When 역ID 2에서 역ID 3까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 2050원이다
    When 역ID 1에서 역ID 4까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 2950원이다
    When 역ID 1에서 역ID 5까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 3250원이다


  Scenario: 50km를 초과하는 거리에 대한 지하철 이용 요금 계산
    Given 지하철역이 등록되어있음
      | name |
      | 역A   |
      | 역B   |
      | 역C   |
      | 역D   |
    And 지하철 노선이 등록되어있음
      | line | upStationId | downStationId | distance | duration |
      | 1호선  | 1           | 2             | 51       | 25       |
      | 2호선  | 2           | 3             | 7        | 30       |
      | 3호선  | 2           | 4             | 8        | 35       |
    When 역ID 1에서 역ID 2까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 2350원이다
    When 역ID 1에서 역ID 3까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 2650원이다
    When 역ID 1에서 역ID 4까지의 최단 거리 경로 요금을 조회하면
    Then 요금은 3150원이다

  Scenario: 로그인한 청소년 사용자가 최단 경로 조회
    Given 청소년 사용자가 로그인되어 있음
    And 지하철역이 등록되어있음
      | name |
      | 역A   |
      | 역B   |
    And 지하철 노선이 등록되어있음
      | line  | upStationId | downStationId | distance | duration |
      | Line1 | 1           | 2             | 10       | 10       |
    When 로그인 사용자가 역ID 1에서 역ID 2까지의 최단 거리 경로 요금을 조회하면
    Then 최단 거리 기준 경로를 응답
      | stationNames |
      | 역A, 역B       |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 10       | 10       |
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 880        |

  Scenario: 로그인한 어린이 사용자가 최단 경로 조회
    Given 어린이 사용자가 로그인되어 있음
    And 지하철역이 등록되어있음
      | name |
      | 역A   |
      | 역B   |
    And 지하철 노선이 등록되어있음
      | line  | upStationId | downStationId | distance | duration |
      | Line1 | 1           | 2             | 10       | 10       |
    When 로그인 사용자가 역ID 1에서 역ID 2까지의 최단 거리 경로 요금을 조회하면
    Then 최단 거리 기준 경로를 응답
      | stationNames |
      | 역A, 역B       |
    And 총 거리와 소요 시간을 함께 응답함
      | distance | duration |
      | 10       | 10       |
    And 지하철 이용 요금도 함께 응답함
      | fareAmount |
      | 550          |
