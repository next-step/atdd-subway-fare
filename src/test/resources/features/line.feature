Feature: 지하철 노선 등록 기능

  Scenario: 지하철 노선을 등록한다
    Given 지하철역들을 생성하고
      | name |
      | 교대역  |
      | 강남역  |
    When 노선들을 생성하고
      | name | color | upStation | downStation | distance | duration |
      | 2호선  | green | 교대역       | 강남역         | 10       | 3        |
    Then 지하철 노선 목록 조회 시 "2호선"을 찾을 수 있다
