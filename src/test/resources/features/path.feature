Feature: 지하철 경로 검색

#
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
      | 2호선  | green  | 교대역       | 강남역         | 10       | 4        |
      | 신분당선 | red    | 강남역       | 양재역         | 10       | 3         |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        | 2         |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance | duration |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 1        |

  Scenario: 두 역의 최단 거리 경로를 조회한다
    When "교대역"과 "양재역"의 "최단"경로를 조회하면
    Then 최단 시간 기준 경로를 응답
    And "교대역,남부터미널역,양재역" 경로가 조회된다
    And 최단거리 "5"가 조회된다

  Scenario: 두 역의 최소 시간 경로를 조회한다
    When "교대역"과 "양재역"의 "최소"경로를 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And "교대역,남부터미널역,양재역" 경로가 조회된다
    And 최소시간 "3"가 조회된다