Feature: 지하철역 경로 찾기 기능
#
#     역삼역    --- *1호선*(10) ---   양재역
#     |                        |
#     *2호선*(10)                   *분당선*(10)
#     |                        |
#     강남역    --- *신분당호선*(10) ---    선릉역
#     <p>
#     강남역    --- *3호선*(10) ---    선릉역
#
  Background: 지하철 노선도 준비
    Given 지하철역들을 생성 요청하고
      | name   |
      | 강남역    |
      | 선릉역    |
      | 양재역    |
      | 역삼역 |
      | 신대방역 |
      | 신림역 |
      | 봉천역 |
    And 지하철 노선들을 생성 요청하고
      | name | color  | upStation | downStation | distance |
      | 신분당선  | green  | 강남역       | 선릉역         | 10       |
      | 분당선 | red    | 선릉역       | 양재역         | 10       |
      | 일호선  | yellow | 양재역       | 역삼역      | 10        |
      | 이호선  | blue | 역삼역       | 강남역      | 10        |
      | 일호선  | black | 신대방역       | 신림역      | 10        |

  Scenario: 실패 경로 조회시 출발역과 도착역이 같은 경우 경로를 조회할 수 없다
    When 경로 조회시 출발역과 도착역이 같은 경우
    Then 출발역과 도착역이 같아 경로 조회를 할 수 없다

  Scenario: 실패경로 조회시 출발역과 도착역이 연결되어 있지 않은 경우 경로를 조회할 수 없다
    When 경로 조회시 출발역과 도착역이 연결되어 있지 않은 경우
    Then 출발역과 도착역이 연결되어 있지 않아 경로 조회를 할 수 없다

  Scenario: 실패 경로 조회시 노선에 존재하지 않는 출발역일 경우 경로를 조회할 수 없다
    When 경로 조회시 존재하지 않는 출발역일 경우
    Then 노선에 존재하지 않는 출발역이여서 경로 조회를 할 수 없다

  Scenario: 실패 경로 조회시 노선에 존재하지 않는 도착역일 경우 경로를 조회할 수 없다
    When 경로 조회시 존재하지 않는 도착역일 경우
    Then 노선에 존재하지 않는 도착역이여서 경로 조회를 할 수 없다

  Scenario: 성공 경로 조회시 출발역과 도착역이 연결되어 있을 경우 경로간 거리를 조회할 수 있다
    When 경로 조회시 출발역과 도착역이 연결되어 최소 거리 기준으로 경로 조회를 요청 하는 경우
    Then 경로간 거리를 조회 할 수 있다

  Scenario: 성공 경로 조회시 출발역과 도착역이 연결되어 있을 경우 경로간 소요시간을 조회할 수 있다
    When 경로 조회시 출발역과 도착역이 연결되어 최소 소요시간 기준으로 경로 조회를 요청 하는 경우
    Then 경로간 소요시간을 조회 할 수 있다