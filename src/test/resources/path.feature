Feature: 경로 조회 기능

  Scenario: 두 역의 최단 경로 조회
#  교대역    --- *2호선*(10) ---   강남역
#  |                        |
#  *3호선*(2)            *신분당선* (10)
#  |                        |
#  남부터미널역  --- *3호선*(3) ---   양재
    Given 지하철역을 생성하고
      | 교대역    |
      | 강남역    |
      | 양재역    |
      | 남부터미널역 |
    And 지하철 노선을 생성하고
      | name | color  | upStation | downStation | distance | duration | extraCharge |
      | 2호선  | green  | 교대역       | 강남역         | 10       | 10       | 300         |
      | 신분당선 | red    | 강남역       | 양재역         | 10       | 10       | 0           |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        | 2        | 1000        |
    And '3호선'에 구간을 생성한다
      | upStation   | 남부터미널역 |
      | downStation | 양재역    |
      | distance    | 3      |
      | duration    | 3      |
    When '교대역'에서 '양재역'까지 최단 경로 조회 요청하면
    Then 최단 경로 응답와 총 거리, 소요 시간, 요금정보를 조회한다
      | stations       | distance | duration | fare |
      | 교대역,남부터미널역,양재역 | 5        | 5        | 1250 |
