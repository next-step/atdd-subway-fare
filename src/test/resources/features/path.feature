Feature: 지하철 경로 관련 기능
  Background:
    # 교대역    --- *2호선/10/1* ---   강남역 --- *4호선/10/2* --- 선릉역
    # |                                |
    # *3호선/10/3*                   *신분당선/10/5*
    # |                                |
    # 남부터미널역  --- *3호선/3/5* ---   양재
    # 가상의역1 --- *가상의노선/60/3* --- 가상의역2

    Given 지하철역 생성을 요청하고
      | name |
      | 교대역 |
      | 강남역 |
      | 남부터미널역 |
      | 양재역 |
      | 선릉역 |
      | 가상의역1 |
      | 가상의역2 |
    And 지하철 노선 생성을 요청하고
      | name | color | upStation | downStation | distance | duration |
      | 이호선 | GREEN | 교대역 | 강남역 | 10 | 1 |
      | 삼호선 | ORANGE | 교대역 | 남부터미널역 | 10 | 3 |
      | 사호선 | BLUE | 강남역 | 선릉역 | 10 | 2 |
      | 신분당선 | RED | 강남역 | 양재역 | 10 | 5 |
      | 가상의노선 | BLACK | 가상의역1 | 가상의역2 | 60 | 3 |
    And 지하철 노선에 구간 생성을 요청한다
      | line | upStation | downStation | distance | duration |
      | 삼호선 | 남부터미널역 | 양재역 | 3 | 5 |

    Scenario: 노선 내 역 간의 경로를 찾을 수 있다
      When "양재역" 과 "남부터미널역" 경로를 조회하면,
      Then "양재역" 과 "남부터미널역" 간 경로가 조회 된다.
      Then 거리는 3이다.

    Scenario: 두 노선에 걸친 역 간의 경로를 찾을 수 있다
      When "양재역" 과 "교대역" 경로를 조회하면,
      Then "양재역" 과 "교대역" 간 경로가 조회 된다.
      Then 거리는 13이다.

    Scenario: 세 노선에 걸친 역 간의 경로를 찾을 수 있다
      When "남부터미널역" 과 "선릉역" 경로를 조회하면,
      Then "남부터미널역" 과 "선릉역" 간 경로가 조회 된다.
      Then 거리는 23이다.

    Scenario: 출발지와 도착지가 연결되어 있지 않으면 실패한다
      When "교대역" 과 "가상의역1" 경로를 조회하면,
      Then 경로가 조회가 실패한다.

    ## 시간 ##
    Scenario: 두 역의 최소 시간 경로를 조회
      When "남부터미널역"에서 "선릉역"까지의 최소 시간 기준으로 경로 조회를 요청
      Then "남부터미널역" 과 "선릉역" 간 경로가 조회 된다.
      Then 시간은 6이다.
      Then 거리는 30이다.

    ## 요금 ##
    Scenario: 10km이내 운임
      When "교대역" 과 "강남역" 경로를 조회하면,
      Then 거리는 10이다.
      Then 시간은 1이다.
      Then 요금은 1250원이다.

    Scenario: 10km초과 50km이내 운임
      When "남부터미널역" 과 "선릉역" 경로를 조회하면,
      Then 거리는 23이다.
      Then 시간은 12이다.
      Then 요금은 1550원이다.

    Scenario: 50km초과 운임
      When "가상의역1" 과 "가상의역2" 경로를 조회하면,
      Then 거리는 60이다.
      Then 시간은 3이다.
      Then 요금은 2250원이다.
