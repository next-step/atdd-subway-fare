Feature: 경로조회 관련 기능
  #
  #     교대역    --- *2호선(10, 2)* ---   강남역
  #     |                        |
  #     *3호선(2, 10)*                   *신분당선(10, 2)*
  #     |                        |
  #     남부터미널역  --- *3호선(3, 10)* ---   양재
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
      | 신분당선    | red   | 강남역   | 양재역   | 10   | 2   |
    And 추가요금이 있는 노선들을 생성 요청하고
      | name   | color   | upStation   | downStation   | distance   | duration   | additionalFee   |
      | 3호선    | orange   | 교대역   | 남부터미널역   | 2   | 10   | 800   |
    And 구간을 등록하고
      | lineName | upStation | downStation | distance | duration   |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 10   |
    And 회원을 등록하고
      | email | password | age |
      | qwe@gmail.com      | 1234    | 17        |
      | zxc@naver.com      | 5678    | 8        |


  Scenario: 요금 정보를 포함한 최단 경로를 조회한다.
    When "교대역"과 "양재역" 사이의 최소거리 경로 조회를 요청하면
    Then "교대역,남부터미널역,양재역" 지하철역을_리턴한다
    And 총 거리 5km와 총 소요 시간 20을 리턴한다
    And 지하철 이용 요금 2050원을 리턴한다

  Scenario: 요금 정보를 포함한 두 역의 최소 시간 경로를 조회
    When "교대역"과 "양재역" 사이의 최소시간 경로 조회를 요청하면
    Then "교대역,강남역,양재역" 지하철역을_리턴한다
    And 총 거리 20km와 총 소요 시간 4을 리턴한다
    And 지하철 이용 요금 1450원을 리턴한다

  Scenario: 청소년 사용자가 요금 정보를 포함한 두 역의 최단 경로를 조회한다.
    When "qwe@gmail.com"사용자가 "교대역"과 "양재역" 사이의 최소거리 경로 조회를 요청하면
    Then "교대역,남부터미널역,양재역" 지하철역을_리턴한다
    And 총 거리 5km와 총 소요 시간 20을 리턴한다
    And 지하철 이용 요금 1360원을 리턴한다

  Scenario: 어린이 사용자가 요금 정보를 포함한 두 역의 최단 경로를 조회한다.
    When "zxc@naver.com"사용자가 "교대역"과 "양재역" 사이의 최소거리 경로 조회를 요청하면
    Then "교대역,남부터미널역,양재역" 지하철역을_리턴한다
    And 총 거리 5km와 총 소요 시간 20을 리턴한다
    And 지하철 이용 요금 850원을 리턴한다