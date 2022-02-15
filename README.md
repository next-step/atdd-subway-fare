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
- [x] 최소 시간 조회 기능
  - [x] 노선 및 구간 정보에 `소요시간` 필드 추가
    - [x] 인수 조건 도출
    - [x] 인수 테스트 수정
    - [x] 문서화
    - [x] 기능 구현
      - [x] 단위 테스트 수정
      - [x] 도메인 필드 추가
      - [x] 기능 구현

Feature: 지하철 경로 탐색

    Scenario: 두 역의 최소 시간 경로 조회
      Given 지하철역이 등록되어 있음
      And 지하철 노선이 등록되어 있음
      And 지하철 노선 지하철역이 등록되어 있음
      When 출발역에서 도착역까지 최소 시간 경로 조회 요청하면
      Then 최소 시간 경로 응답
      And 총 거리와 소요 시간, 요금정보를 함께 응답

    Scenario: 두 역의 최단 경로 조회
      Given 지하철역이 등록되어 있음
      And 지하철 노선이 등록되어 있음
      And 지하철 노선 지하철역이 등록되어 있음
      When 출발역에서 도착역까지 최단 경로 조회 요청하면
      Then 최단 경로 응답
      And 총 거리와 소요 시간, 요금정보를 함께 응답

### 1단계 피드백
- [x] 메서드 분리 리팩토링
  - 필드 추가 등으로 변경해야할 코드가 많다면 메소드 분리를 통해 수정 범위를 줄이자.
- [x] 문서화 테스트에서 인수 테스트 재사용
  - 문서화 테스트와 인수 테스트는 거의 동일한 코드로 이루어져 있으니 적절히 조합해서 사용할 수 있도록 분리해보자.

### 2단계 - 요금 조회
- [x] 경로 조회 결과에 요금정보 포함
  - 인수테스트(수정) -> 문서화 -> 기능구현
  - [x] 인수 테스트 수정
    - [x] 요금정보 검증 추가
  - [x] 문서화 테스트
    - [x] 응답 결과에 요금정보 추가
  - [x] 요금 계산 기준 기능 구현
    - [x] 요금 계산 기준 단위 테스트
      - 기본운임(10km 이내) : 1,250
      - 10km 초과 ~ 50km 이내 : 5km 마다 100원 부과
      - 50km 초과 : 8km 마다 100원 부과
    - [x] 요금 계산 기능 구현

### 2단계 - 피드백
- [x] 인수테스트 스텝, 문서화테스트 스텝 리팩터링
  - 재사용 할 수 있는 비슷한 코드가 많으니 인수/문서화 에서 함께 사용할 수 있는 메서드로 변경
- [x] 서비스의 요금 계산 로직을 도메인으로 이동
  - 요금 계산의 역할을 서비스가 할 이유가 없다.. 비즈니스 로직은 도메인으로!

### 3단계 - 요금 정책 추가
- [x] 인수 테스트 수정
  - [x] 노선 추가 요금
  - [x] 경로 조회 로그인
- [x] 문서화 테스트 수정
  - [x] 추가 요금
- [x] 요금 정책 기능 구현
  - [x] 노선별 추가 요금
    - [x] 노선에 추가 요금 필드(nullable) 추가
  - [x] 요금 계산
    - [x] 추가 요금이 있는 여러 노선들을 이용한 경우 가장 높은 추가 요금만 적용 
  - [x] 연령별 할인 추가
    - [x] 거리비례제로 계산된 요금에 연령별 할인 적용
      - 6세이상 ~ 13세미만 : 350원 공제 후 50%할인
      - 13세이상 ~ 19세미만 : 350원 공제 후 20%할인
  - [x] 로그인 사용자 할인 정책 적용
