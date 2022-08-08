# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

#Step2 TODO List
- [ ] 요금 정보 조회
    - [x] 인수 조건 도출
    - [x] 인수 테스트 작성
    - [ ] 경로 조회시 요금 정보 반환 기능 구현
- [ ] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요
- [ ] 커밋 단위를 작게 하기

#Step2 - 인수 조건
```
Feature: 지하철 경로 검색시 요금과 함께 응답하기 

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
    And 지하철 이용 요금도 함께 응답함
```

#Step1 PR 수정 TODO List
- [x] PathAcceptanceTest 에서 환승, 반대 방향의 경우도 테스트 작성
- [x] document(), requestParameters(), responseFields() 메서드로 분리하기
- [x] LineTest의 given절, duration 인자 중복 제거
- [x] Sections 기간에 대한 단위 테스트 추가

#Step1 TODO List
- [x] 경로 조회 시 최소 시간 기준으로 조회되도록 구현
- [x] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보 추가하기.
- [x] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [x] 커밋 단위를 작게 하기

#Step1 - 인수 조건
```
Feature: 지하철 경로 검색

  Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
```
