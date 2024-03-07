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
- [ ] 노선추가 & 구간 추가 시 소요시간 정보 추가
  - [ ] 노선 인수 테스트 수정 -> cucumber 전환
  - [ ] Section, SectionRequest, LineRequest 객체에 소요시간 추가
  - [ ] Sections - addSection, addMidSection 메소드 수정
  - [ ] LineService - saveLine 수정

- [ ] 경로 조회 시 최소 시간 기준으로 조회 기능 추가
  - [ ] 경로 조회 인수 테스트 수정 
    - type(거리/시간) 별로 조회
  - [ ] 경로 조회 조건에 type 추가
  - [ ] PathFinder 객체에 type 추가 및 type 별 경로 조회 기능 구현