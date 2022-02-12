<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

### 실습
- [x] 경로찾기 기능 문서화용 테스트 작성
  - [x] 문서가 나오도록 테스트 코드 작성 (문서화)
    - adoc 용법을 몰라서 request parameters와 response fields 나오도록 하는데 조금 헤맸다..
  - [x] 중복제거 리팩토링 (PathSteps)
    - 인수테스트보다 상대적으로 코드량이 많다 보니 리팩토링을 해도 조금 덜 깔끔한 느낌이다.  

### 1단계 - 경로 조회 타입 추가
- [ ] 최소 시간 조회 기능
  - [ ] 노선 및 구간 정보에 `소요시간` 필드 추가
    - [x] 인수 조건 도출
    - [x] 인수 테스트 수정
    - [x] 문서화
    - [ ] 기능 구현
      - [x] 단위 테스트 수정
      - [ ] 도메인 필드 추가
      - [ ] 기능 구현

Feature: 지하철 경로 탐색

    Scenario: 두 역의 최소 시간 경로 조회
      Given 지하철역이 등록되어 있음
      And 지하철 노선이 등록되어 있음
      And 지하철 노선 지하철역이 등록되어 있음
      When 출발역에서 도착역까지 최소 시간 경로 조회 요청하면
      Then 최소 시간 경로 응답
      And 총 거리와 소요 시간을 함께 응답

    Scenario: 두 역의 최단 경로 조회
      Given 지하철역이 등록되어 있음
      And 지하철 노선이 등록되어 있음
      And 지하철 노선 지하철역이 등록되어 있음
      When 출발역에서 도착역까지 최단 경로 조회 요청하면
      Then 최단 경로 응답
      And 총 거리와 소요 시간을 함께 응답

