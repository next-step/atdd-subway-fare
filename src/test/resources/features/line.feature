Feature: 지하철 노선 관련 기능
  Background: 지하철 역 준비
    Given 지하철역들을 생성하고
      | name    |
      | 도곡역   |
      | 학여울역 |
      | 개포동역 |

  Scenario: 지하철 노선을 생성하고 목록을 조회한다.
    Given 지하철 노선을 생성하고
      | name | color | startStation | endStation | distance | duration | additionalFee |
      | 분당선 | 노랑  |     도곡역    |    학여울역  |     6    |    4     |    400     |
      | 삼호선 | 주황  |     도곡역    |    개포동역  |     4    |    5     |    0     |
    When 지하철 노선 목록을 조회하면
    Then 조회한 지하철 노선 목록에서 생성한 "분당선,삼호선" 노선을 찾을 수 있다

  Scenario: 지하철 노선을 조회한다.
    Given 지하철 노선을 생성하고
      | name | color | startStation | endStation | distance | duration | additionalFee |
      | 분당선 | 노랑  |     도곡역    |    학여울역 |    6      |   4     |    400     |
    When 생성한 "분당선" 노선을 조회하면
    Then 생성한 "분당선" 노선의 정보가 조회된다
