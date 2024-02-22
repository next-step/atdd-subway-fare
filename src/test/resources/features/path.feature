Feature: 지하철역 경로 찾기 기능
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
      | 2호선  | green  | 교대역       | 강남역         | 5        | 4        |
      | 신분당선 | red    | 강남역       | 양재역         | 10       | 4        |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        | 3        |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance | duration |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 2        |

  Scenario: 두 역의 최단 거리 경로를 조회한다
    When "강남역"과 "남부터미널역"의 최단 거리 경로를 조회하면
    Then "강남역,교대역,남부터미널역" 경로와 거리 7, 소요시간 7이 조회된다

  Scenario: 두 역의 최단 시간 경로를 조회한다
    When "강남역"과 "남부터미널역"의 최단 시간 경로를 조회하면
    Then "강남역,양재역,남부터미널역" 경로와 거리 13, 소요시간 6이 조회된다

