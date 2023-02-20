# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 실습 - 문서화

### 실습 - 테스트를 통한 문서화

- 경로 찾기 기능을 문서화하기 위한 테스트를 작성하세요.
- 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정하세요.

### 실습 방법

- PathDocumentation의 path 메서드를 완성시키세요.
- PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요.
- gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요
- build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요.
- PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요

### 힌트

#### PathDocumentation 완성하기

##### 요청 시 문서화 설정 포함

- 문서화를 위한 RequestSpecification 와 filter를 적용
- spec은 Documentation 클래스에 정의되어 있음

##### 서비스 레어이 Stubbing

- 실제 기능이 정상동작하기 위함을 확인하는것을 검증하는 목적이 아니라서 요청과 응답을 검증하기 위한 부분만 구현

##### 문서화 커스터마이징

- 추가적인 설정을 부여할 경우 새로운 adoc 파일이 생성됨
- 기존 adoc 문서에 include 하여 문서에 노출

###### Request Parameters

https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-parameters

###### Request and Response Fields

https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-response-payloads-fields

##### 중복 제거

- spec을 설정하는 부분과 filter 설정하는 부분을 RequestSpecification 객체로 주입 받을 경우 나머지 RestAssured 부분은 인수 테스트의 Step과 함께 사용할 수 있음

### 실습 - 구현 사항

- [X] PathDocumentation의 path 메서드를 완성
- [X] PathDocumentation의 테스트를 수행시켜 snippet을 생성
  - `build/generated-snippets` 경로 하위에 `document` 폴더명 및 `*.adoc` 파일 생성 확인 완료
- [X] gradle로 asciidoctor task를 수행시켜 문서 파일을 생성
  - `./gradlew asciidoctor` 명령어 수행 
  - `src/docs/asciidoc` 경로 하위에 `*.adoc` 파일 확인 완료
- [X] build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함
  - 이미지 첨부 및 `output` 폴더 아래도 첨부
- [X] PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거
  - `PathSteps` 클래스로 리팩토링
가
## 🚀 1단계 - 경로 조회 타입 추가

### 요구사항

#### 기능 요구사항 - 최소 시간 경로 타입 추가

- [] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- [] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
- [] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

#### 프로그래밍 요구사항

- 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
  - 요구사항 설명을 참고하여 인수 조건을 정의
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- 인수 테스트 이후 기능 구현은 TDD로 진행하세요.
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

### 요구사항 설명

#### 인수 조건

```text
Feature: 지하철 경로 검색

  Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
```

#### 요청 api 수정

- source: 출발역 id
- target: 도착역 id
- type: 거리 or 시간

```http
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=3&type=DURATION
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

#### 소요 시간 추가

- 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.

```java
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
}
```

### 실습 - 구현 사항

- [] 인수 조건 정의
  - 인수 테스트 성공 케이스 추가