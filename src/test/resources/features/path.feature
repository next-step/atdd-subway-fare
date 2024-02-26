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
      | name | color  | upStation | downStation | distance | duration | surcharge |
      | 2호선  | green  | 교대역       | 강남역         | 10       | 10       | 0       |
      | 신분당선 | red    | 강남역       | 양재역         | 10       | 10       | 900       |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        | 2        | 0       |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance | duration |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 3        |

  Scenario: 두 역의 최단 거리 경로를 조회한다
    When "교대역"과 "남부터미널역"의 "DISTANCE"경로를 조회하면
    Then "교대역,남부터미널역" 경로가 조회된다
    And 총 거리 "2"와 소요 시간 "2"을 함께 응답함
    And 지하철 이용 요금 "1250"을 함께 응답함

  Scenario: 두 역의 최소 시간 경로를 조회
    When "교대역"과 "양재역"의 "DURATION"경로를 조회하면
    Then "교대역,남부터미널역,양재역" 경로가 조회된다
    And 총 거리 "5"와 소요 시간 "5"을 함께 응답함

  Scenario: 추가 요금이 있는 경로를 이용한다
    When "강남역"과 "양재역"의 "DISTANCE"경로를 조회하면
    Then "강남역,양재역" 경로가 조회된다
    And 총 거리 "10"와 소요 시간 "10"을 함께 응답함
    And 지하철 이용 요금 "2150"을 함께 응답함
