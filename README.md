# 🚀 1단계 - 경로 조회 타입 추가

---

## 최소 시간 경로 타입 추가
### 인수조건
~~~
Feature: 지하철 경로 검색

  Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
~~~

### 요청 api 수정
- source: 출발역 id
- target: 도착역 id
- type: 거리 or 시간
~~~
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=3&type=DURATION
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
~~~

### 소요 시간 추가
- 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.
~~~
public class LineRequest {
...

private int distance;
private int duration;

...
~~~

## TODO
- [x] 노선 추가 & 구간 추가 시 소요시간 정보 추가
  - [x] 노선 인수 테스트 수정 -> cucumber 전환
  - [x] SectionRequest, LineRequest 객체에 소요시간 추가
  - [x] Section 객체에 소요시간 추가
  - [x] LineService - saveLine 수정

- [x] 경로 조회 시 최소 시간 기준으로 조회 기능 추가
  - [x] 경로 조회 인수 테스트 수정 
    - type(거리/시간) 별로 조회
  - [x] 경로 조회 조건에 type 추가
  - [x] PathFinder 객체에 type 추가 및 type 별 경로 조회 기능 구현
- [x] 즐겨찾기 등록, 조회 시 type 추가 

---

# 🚀 2단계 - 요금 조회

---

## 경로 조회 시 요금 정보 포함하기
### 인수 조건
~~~
Feature: 지하철 경로 검색

Scenario: 두 역의 최단 거리 경로를 조회
Given 지하철역이 등록되어있음
And 지하철 노선이 등록되어있음
And 지하철 노선에 지하철역이 등록되어있음
When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
Then 최단 거리 경로를 응답
And 총 거리와 소요 시간을 함께 응답함
And 지하철 이용 요금도 함께 응답함
~~~

### 요금 계산 방법
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
  - 10km초과∼50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)
  - 9km = 1250원
  - 12km = 10km + 2km = 1350원
  - 16km = 10km + 6km = 1450원

## TODO
- [x] 경로 인수 테스트 수정
  - [x] 경로 조회 시, 요금 정보 응답 확인

- [x] 요금 계산 구현
  - [x] 요금 계산 객체 - FareCalculator
    - [x] 단위 테스트 작성
      - 10km
      - 11 ~ 49km
      - 50km
      - 51 ~ km
  - [x] calculateFare 구현

--- 

# 🚀 3단계 - 요금 정책 추가

--- 

## 추가된 요금 정책

### 노선별 추가 요금
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

### 로그인 사용자의 경우 연령별 요금으로 계산
- 청소년: 운임에서 350원을 공제한 금액의 20%할인
- 어린이: 운임에서 350원을 공제한 금액의 50%할인
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만

## TODO
- [ ] 경로 인수 테스트 수정
  - [x] 노선별 추가 요금 반영
  - [ ] 로그인 사용자 케이스 추가
- [ ] 추가 요금 정책에 대한 도메인 테스트 추가
  - [x] 노선별 추가 요금
  - [ ] 연령별 요금
- FarePolicy 추가
  - [x] LinePolicy
    - [x] Line 객체에 additionalFee 변수 추가
  - [ ] AgePolicy
