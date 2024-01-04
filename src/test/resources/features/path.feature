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
      | name | color  | upStation | downStation | distance |
      | 2호선  | green  | 교대역       | 강남역         | 10       |
      | 신분당선 | red    | 강남역       | 양재역         | 10       |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance |
      | 3호선      | 남부터미널역    | 양재역         | 3        |

  Scenario: 두 역의 최단 거리 경로를 조회한다
    When "교대역"과 "양재역"의 경로를 조회하면
    Then "교대역,남부터미널역,양재역" 경로가 조회된다