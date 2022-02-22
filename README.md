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

## 실습 - 테스트를 통한 문서화
- 경로 찾기 기능을 문서화하기 위한 테스트를 작성하세요.
- 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정하세요.

### 실습 방법
- PathDocumentation의 path 메서드를 완성시키세요.
- PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요.
- gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요
- build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요.
- PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요

## 🚀 1단계 - 경로 조회 타입 추가
### 요구사항 - 최소 시간 경로 타입 추가
- [x] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- [x] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
- [x] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [x] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

### 요구사항 설명
#### 인수 조건
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
#### 소요 시간
- 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.
~~~
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    
    ...
~~~

## 🚀 2단계 - 요금 조회

### 요구사항 - 경로 조회 시 요금 정보 포함하기
- [x] 경로 조회 결과에 요금 정보를 포함하세요.
- [x] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [x] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

### 요구사항 설명
#### 인수 조건
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
#### 소요 시간
- [x] 기본운임(10㎞ 이내) : 기본운임 1,250원
- [x] 이용 거리초과 시 추가운임 부과
  - [x] 10km초과∼50km까지(5km마다 100원)
  - [x] 50km초과 시 (8km마다 100원)
- [x] 지하철 운임은 거리비례제로 책정됩니다. (실제 경로가 아닌 최단거리 기준)
~~~
9km = 1250원
12km = 10km + 2km = 1350원
16km = 10km + 6km = 1450원
~~~

## 🚀 3단계 - 요금 정책 추가 

### 요구사항 - 스펙 추가하기
- [] 추가된 요금 정책을 반영하세요.
- [] 인수 테스트 변경 -> 문서화 변경 -> 기능 구현 순으로 진행하세요.
- [] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

### 요구사항 설명
#### 추가된 요금 정책
- [] 노선별 추가 요금
  - [] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
  - [] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
  
#### 로그인 사용자의 경우 연령별 요금으로 계산
- [] 청소년 (13세~18세): 운임에서 350원을 공제한 금액의 20%할인 
- [] 어린이 (6세~12세): 운임에서 350원을 공제한 금액의 50%할인