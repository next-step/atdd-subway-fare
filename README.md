# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 인수 테스트와 리팩터링
## 실습 - Cucumber 전환
### 요구사항
- [x] test/resources/features/station.feature 파일의 지하철역을 생성한다 Scenario를 실행하기
- [x] 경로 조회 인수 테스트 cucumber 전환하기

## 1단계 - 경로 조회 타입 추가
### 요구사항
- [x] 경로 조회 시 최소 시간 기준으로 조회기능 추가
  - 요청 API 수정
  ```shell
  HTTP/1.1 200
  Request method: GET
  Request URI: http://localhost:55494/paths?source={출발역id}&target={도착역id}&type={거리or시간}
  Headers: Accept=application/json
           Content-Type=application/json; charset=UTF-8
  ```
- [x] 노선추가 & 구간 추가 시 거리, 소요시간 정보 추가
  - 소요 시간 추가
  ```java
  public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    ...
  }
  ```
- [x] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행
- [x] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현

### 인수 조건
- Feature: 지하철 경로 검색
>Scenario: 두 역의 최소 시간 경로를 조회<br>
Given 지하철역이 등록되어있음<br>
And 지하철 노선이 등록되어있음<br>
And 지하철 노선에 지하철역이 등록되어있음<br>
When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청<br>
Then 최소 시간 기준 경로를 응답<br>
And 총 거리와 소요 시간을 함께 응답함
