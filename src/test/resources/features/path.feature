Feature: 지하철역 경로 탐색 관련 기능
  Background: 지하철 노선 초기 데이터 설정
    Given 지하철역을 생성하고
    | name |
    | 교대역  |
    | 강남역  |
    | 역삼역  |
    | 양재역  |
    | 남부터미널역|
    | 석남역   |
    And 지하철 노선을 생성하고
    | name | color | upStation | downStation | distance | transitTime |
    | 2호선  | green | 교대역       | 강남역         | 10       | 6           |
    | 신분당선 | red   | 강남역       | 양재역         | 4        | 3           |
    | 3호선  | orange| 교대역       | 남부터미널역      | 6        | 2           |
    And 구간을 추가한다
    | name | upStation | downStation | distance | transitTime |
    | 2호선  | 강남역       | 역삼역         | 2        | 5           |
    | 3호선  | 남부터미널역    | 양재역         | 3        | 4           |

  Scenario: 지하철 경로 탐색에 성공한다.
    When "남부터미널역"과 "역삼역"의 경로 탐색을 수행하면
    Then 지하철역 경로 탐색 결과가 반환된다.

  Scenario: 출발역과 도착역이 같아 지하철 경로 탐색에 실패한다.
    When "남부터미널역"을 출발역과 도착역으로 하여 경로 탐색을 수행하면
    Then 지하철역 경로 탐색에 실패하고, 에러 Http Status를 반환한다.

  Scenario: 출발역과 도착역이 연결되어 있지 않아 지하철 경로 탐색에 실패한다.
    When 연결되지 않은 "석남역"과 "남부터미널역"에 대해 경로 탐색을 수행하면
    Then 지하철역 경로 탐색에 실패하고, 에러 Http Status를 반환한다.

  Scenario: 존재하지 않는 출발역이나 도착역을 조회하여 지하철 경로 탐색에 실패한다.
    When 등록되지 않은 "안산역"과 존재하는 "남부터미널역"에 대하여 경로 탐색을 수행하면
    Then 지하철역 경로 탐색에 실패하고, 에러 Http Status를 반환한다.

  Scenario: 두 역의 최소 시간 경로를 조회
    When "교대역"에서 "양재역"까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    | transitTime | 6 |
    And 총 거리와 소요 시간을 함께 응답함

  Scenario: 두 역의 최단 거리 경로를 조회
    When "교대역"에서 "양재역"까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 기준 경로를 응답
    | distance | 9 |
    And 총 거리와 소요 시간을 함께 응답함
    And 지하철 이용 요금도 함께 응답함
    | fare | 1250 |