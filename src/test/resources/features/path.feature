Feature: 지하철 경로 관련 기능

#     교대역    --- *2호선* ---   강남역
#     |                        |
#     *3호선*                   *신분당선*
#     |                        |
#     남부터미널역  --- *3호선* ---   양재
  Background: 지하철 노선도 준비
    Given 지하철역들을 생성 요청하고
      | name   |
      | 교대역    |
      | 강남역    |
      | 양재역    |
      | 남부터미널역 |
    And 지하철 노선들을 생성 요청하고
      | name | color  | upStation | downStation | distance | duration |
      | 2호선  | green  | 교대역     | 강남역        | 10       | 5        |
      | 신분당선 | red    | 강남역    | 양재역        | 10       | 5        |
      | 3호선  | orange | 교대역     | 남부터미널역    | 2        | 10       |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance | duration |
      | 3호선     | 남부터미널역  | 양재역        | 3        | 2        |

  Scenario: 두 역의 최단 거리 경로를 조회한다
    When "교대역"과 "양재역"의 경로를 조회하면
    Then "교대역,남부터미널역,양재역" 경로가 조회된다


  Scenario: 두 역의 최소 시간 경로를 조회
    When "교대역"에서 "양재역"까지의 최소 시간 기준으로 경로 조회를 요청
    Then 총 거리는 "20"이고 최소 시간은 "10"이다.
