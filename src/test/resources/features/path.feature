Feature: 경로조회 관련 기능
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
      | 남부터미널역    |
    And 노선들을 생성 요청하고
      | name   | color   | upStation   | downStation   | distance   | duration   |
      | 2호선    | green   | 교대역   | 강남역   | 10   | 2   |
      | 신분당선    | red   | 강남역   | 양재역   | 10   | 3   |
      | 3호선    | orange   | 교대역   | 남부터미널역   | 2   | 2   |
    And 구간을 등록하고
      | lineName | upStation | downStation | distance | duration   |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 2   |

  Scenario: 최단 경로를 조회한다.
    When "교대역"과 "양재역" 사이의 경로 조회를 요청하면
    Then "교대역,남부터미널역,양재역" 지하철역을_리턴한다