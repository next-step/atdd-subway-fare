Feature: 지하철 경로 검색 기능
  Background:
  #    지하철 노선도
  #               3km / 1min     1km / 3min
  #            강남 --------- 역삼 --------- 선릉
  #             |                          |
  #             |                          |   1km / 1min
  # 1km / 1min  |                         한티
  #             |                          |
  #             |                          |   1km / 1min
  #            양재 --------- 매봉 --------- 도곡
  #               1km / 1min    1km / 1min
  #
  #            남포 --------- 서면
  #               5km / 5min
    Given 지하철 역을 생성하고
      | name     |
      | 강남역     |
      | 역삼역     |
      | 선릉역     |
      | 한티역     |
      | 양재역     |
      | 매봉역     |
      | 도곡역     |
      | 남포역     |
      | 서면역     |
    And 지하철 노선을 생성하고
      | name   |   color  | upStationName | downStationName | distance |  duration  |
      | 2호선   |    초록   |      강남역     |       역삼역      |     3    |      1     |
      | 3호선   |    주황   |      양재역     |       매봉역      |     1    |      1     |
      | 신분당선  |   빨강    |     강남역      |      양재역       |     1    |      1     |
      | 분당선   |    노랑   |     선릉역      |       한티역      |     1    |      1     |
      | 부산선   |    검정   |     남포역      |       서면역      |     5    |      5     |
    And 지하철 노선의 구간들을 생성한다
      | name   | upStationName | downStationName | distance |  duration  |
      | 2호선   |      역삼역     |      선릉역       |     1    |      3     |
      | 3호선   |      매봉역     |      도곡역       |     1    |      1     |
      | 분당선   |      한티역     |      도곡역       |     1    |      1     |

    Scenario: 두 역 사이의 최단 거리 경로를 조회한다.
      When "매봉역"과 "역삼역"의 최단 거리 경로를 조회하면
      Then 두 역을 잇는 경로 중 거리가 가장 짧은 경로를 반환한다.
        | 매봉역 |
        | 도곡역 |
        | 한티역 |
        | 선릉역 |
        | 역삼역 |
      And 총 이동거리는 4km 소요시간은 6분이다.

    Scenario: 두 역 사이의 최단 시간 경로를 조회한다.
      When "매봉역"과 "역삼역"의 최단 시간 경로를 조회하면
      Then 두 역을 잇는 경로 중 소요시간이 가장 짧은 경로를 반환한다.
        |  매봉역   |
        |  양재역   |
        |  강남역   |
        |  역삼역   |
      And 총 이동거리는 5km 소요시간은 3분이다.

    Scenario: 이어지지 않는 두 역의 경로를 조회한다.
      When "강남역"과 "서면역"의 경로를 조회하면
      Then 에러가 발생한다.
