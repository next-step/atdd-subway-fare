# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 실습 - 테스트를 통한 문서화
### 기능 요구사항
- [x] 경로 찾기 기능을 문서화하기 위한 테스트 작성
- [x] 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정

### 프로그래밍 요구사항
- [x] PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 삭제.

## 1단계 - 경로 조회 타입 추가
### 기능 요구사항 
- [x] 경로조회 기능 수정
  - [x] 요청 항목에 조회 타입 코드 추가 - 거리 or 시간
  - [x] 타입 코드가 시간이면, 최소 시간 경로를 조회
  - [x] 타입 코드가 경로면, 최단 거리 경로를 조회
  - [x] 응답 항목에 소요시간과 거리 추가
- [x] 노선 추가 기능 수정
  - [x] 요청 항목에 거리와 소요시간 정보 추가
- [x] 구간 추가 기능 수정
  - [x] 요청 항목에 거리와 소요시간 정보 추가
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능 구현 (인수테스트 -> 문서화 -> 기능 구현)
- [x] 인수 조건을 검증하는 인수 테스트 작성
- [x] 인수 테스트를 충족하는 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
- [x] 인수 테스트 이후 기능 구현은 TDD로 진행
- [x] 도메인 레이어 테스트는 필수
- [x] 서비스 레이어 테스트는 선택
- [x] 개발 흐름을 알 수 있도록 작은 단위로 커밋하기


## 2단계 - 요금 조회
### 기능 요구사항
- [ ] 경로조회 기능 수정 - 응답에 요금정보 추가
  - [ ] 10km까지는 기본운임으로 1,250원이다.
  - [ ] 이용 거리초과 시 추가운임비용을 부과한다.
    - [ ] 10km초과∼50km까지는 5km마다 100원을 추가한다.
    - [ ] 50km초과 시 8km마다 100원을 추가한다.
### 프로그래밍 요구사항
- [ ] 인수 테스트 주도 개발 프로세스에 맞춰서 기능 구현 (인수테스트 -> 문서화 -> 기능 구현)
- [ ] 인수 조건을 검증하는 인수 테스트 작성
- [ ] 인수 테스트를 충족하는 기능 구현
- [ ] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
- [ ] 인수 테스트 이후 기능 구현은 TDD로 진행
- [ ] 도메인 레이어 테스트는 필수
- [ ] 서비스 레이어 테스트는 선택
- [ ] 개발 흐름을 알 수 있도록 작은 단위로 커밋하기
