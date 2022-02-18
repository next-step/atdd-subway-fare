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

# 🚀 1단계 - 경로 조회 타입 추가
# 요구사항

---

- [x]  경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능 추가
- [x]  노선 추가 & 구간 추가 시 `거리`와 함께 `소요 시간` 정보 추가
  → 구간 거리는 기존 구간 거리보다 클 수 없어서 Wrapping 클래스를 만들었지만 구간 시간은 따로 제약이 없기 때문에 만들지 않음.
- [x]  인수 테스트(수정) → 문서화 → 기능 구현 순으로 진행
- [x]  개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현

## 세부 요구 사항

세부 요구 사항 다시 정리하기.

- [x]  두 역의 최소 시간 인수 테스트 작성
- [x]  문서화
- [x]  테스트 통과를 위해 구현
- [x]  위의 사항 리팩토링
    - [x]  최단 거리, 최소 시간 분기 코드 리팩토링
    - [x]  경로 타입은 enum으로 변경
- [x]  상세 케이스 도출
    - [x]  최소 시간 조회 시 거리는 최소 거리가 아닌 경로의 총 거리
      → 진행 중.. 어케 하지??
      → `shortsPathStations`()에서 나온 역으로 구간을 찾는다. 역으로만 찾으면 된다. 그걸 합친다.
    - [x]  최단 거리 조회 시 시간은 최소 시간이 아닌 경로의 시간
- [ ]  리팩토링

# 요구사항 설명

---

## 인수 조건

```java
Feature: 지하철 경로 검색

  Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함

```

## 소요 시간 추가

- 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.

```java
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;

    ...
```