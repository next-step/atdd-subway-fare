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

## 2단계 - 요금 조회
### 요구사항
#### 경로 조회 시 요금 정보 포함
- [x] 경로 조회 결과에 요금 정보를 포함
  - 기본운임(10km 이내) : 기본운임 1,250원
  - 이용 거리초과 시 추가운임 부과
    - 10km 초과 ~ 50km 이하 - 5km 마다 100원 추가
    - 50km 초과 - 8km 마다 100원 추가
    > 9km = 1250원<br>
    12km = 10km + 2km = 1350원<br>
    16km = 10km + 6km = 1450원

### 인수 조건
- Feature: 지하철 경로 검색
>  Scenario: 두 역의 최단 거리 경로를 조회 <br>
Given 지하철역이 등록되어있음 <br>
And 지하철 노선이 등록되어있음 <br>
And 지하철 노선에 지하철역이 등록되어있음 <br>
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청 <br>
Then 최단 거리 경로를 응답 <br>
And 총 거리와 소요 시간을 함께 응답함 <br>
And 지하철 이용 요금도 함께 응답함

## 3단계 - 요금 정책 추가
### 요구사항
#### 노선별 추가 요금
- [ ] 노선 등록시 추가요금 정보 저장
- [ ] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 + 900원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,250원 + 100원 + 900원 -> 2,250원
- [ ] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  - ex) 0원, 500원, 900원의 추가 요금이 발생하는 노선들을 경유하여 8km 이용 시 1,250원 + 900원 -> 2,150원
- [ ] 로그인 사용자의 경우 연령별 요금으로 계산
  - 청소년(13세 이상 ~ 19세 미만): 운임에서 350원을 공제한 금액의 20% 할인
  - 어린이(6세 이상 ~ 13세 미만): 운임에서 350원을 공제한 금액의 50% 할인

### 인수 조건
- Feature: 지하철 경로 검색
>  Scenario: 두 역의 최단 거리 경로가 한가지 노선이용만인 경우의 경로 조회 <br>
Given 지하철역이 등록되어있음 <br>
And 지하철 노선이 등록되어있음 <br>
And 지하철 노선에 지하철역이 등록되어있음 <br>
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청 <br>
Then 최단 거리 경로를 응답 <br>
And 총 거리와 소요 시간을 함께 응답함 <br>
And 지하철 이용 요금도 함께 응답함

>  Scenario: 두 역의 최단 거리 경로가 두가지 노선이용인 경우의 경로 조회 <br>
Given 지하철역이 등록되어있음 <br>
And 지하철 노선이 등록되어있음 <br>
And 지하철 노선에 지하철역이 등록되어있음 <br>
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청 <br>
Then 최단 거리 경로를 응답 <br>
And 총 거리와 소요 시간을 함께 응답함 <br>
And 지하철 이용 요금도 함께 응답함

>  Scenario: 청소년이 로그인 한 뒤 두 역의 최단 거리 경로를 조회 <br>
Given 지하철역이 등록되어있음 <br>
And 지하철 노선이 등록되어있음 <br>
And 지하철 노선에 지하철역이 등록되어있음 <br>
And 나이가 13살인 사용자로 로그인 되어있음 <br>
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청 <br>
Then 최단 거리 경로를 응답 <br>
And 총 거리와 소요 시간을 함께 응답함 <br>
And 지하철 이용 요금도 함께 응답함

>  Scenario: 어린이가 로그인 한 뒤 두 역의 최단 거리 경로를 조회 <br>
Given 지하철역이 등록되어있음 <br>
And 지하철 노선이 등록되어있음 <br>
And 지하철 노선에 지하철역이 등록되어있음 <br>
And 나이가 6살인 사용자로 로그인 되어있음 <br>
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청 <br>
Then 최단 거리 경로를 응답 <br>
And 총 거리와 소요 시간을 함께 응답함 <br>
And 지하철 이용 요금도 함께 응답함
