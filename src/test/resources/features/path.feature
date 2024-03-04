Feature: 지하철 경로 검색

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
      | 2호선  | green  | 교대역       | 강남역         | 10       | 1        |
      | 신분당선 | red    | 강남역       | 양재역         | 10       | 2         |
      | 3호선  | orange | 교대역       | 남부터미널역      | 2        | 3         |
    And 지하철 구간을 등록 요청하고
      | lineName | upStation | downStation | distance | duration |
      | 3호선      | 남부터미널역    | 양재역         | 3        | 4        |

  Scenario Outline: 두 역의 <type> 경로를 조회한다
    When <source>과 <target>의 <type> 경로를 조회하면
    Then <stations> 경로가 조회된다
    And 총 거리 <distance>km와 소요시간 <duration>분을 함께 응답한다

    Examples:
      | type | source | target | stations       | distance | duration |
      | "최단거리" | "교대역"    | "양재역"    | "교대역,남부터미널역,양재역" | 5 | 7  |
      | "최소시간" | "교대역"    | "양재역"    | "교대역,강남역,양재역"    | 20  | 3  |
