# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션



# merge 이후 미션 수행 명령어
```
git checkout songteaheon  
git fetch upstream songteaheon  
git rebase upstream/songteaheon
```  

# Study
- https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/#getting-started
- Documentation test에서 RANDOM_PORT를 사용한 이유: 다른 테스트와 병렬로 수행하면서 PortInUseException이 발생할 수 있기 때문.


# 할 일
## 0단계 - 뼈대 코드 준비 & 문서화 실습
- [X] 경로 찾기 기능을 문서화하기 위한 테스트를 작성하세요.
  - [X] PathDocumentation의 path 메서드를 완성시키세요. 
  - [X] PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요. 
  - [X] gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요 
  - [X] build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요. 
  - [X] PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요 
- [X] 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정하세요.

## 1단계
- [X] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
  - [X] 인수 테스트 (수정)
  - [X] 문서화
  - [X] 단위 테스트 작성
  - [X] 기능 구현
  - [X] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.
- [X] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
  - [X] 인수 테스트 (수정)
  - [X] 문서화
  - [X] 단위 테스트 작성
  - [X] 기능 구현
  - [X] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

## 2단계
- [X] 1단계 리뷰 반영
  - [X] SubwayMapTest 수정 - 질문하기 ParameterizedTest
  - [X] PathAcceptanceTest 수정
- [X] 경로 조회 결과에 요금 정보를 포함하세요. (실제 경로가 아닌 최단거리 기준으로 금액을 계산한다.)
  - [X] 인수 테스트 (수정)
  - [X] 문서화
  - [X] 단위 테스트 작성
  - [X] 코드 수정

## 3단계
- [X] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - [X] 노선 생성 시, 추가요금 설정할 수 있도록 수정
    - [X] 인수 테스트 (수정)
    - [X] 문서화
    - [X] 단위 테스트 작성 (라인 생성, 라인 조회)
    - [X] 코드 수정
  - [X] 경로 조회 시, 노선에 대한 추가 요금이 붙어서 요금이 나오는지 확인(경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용)
    - [X] 인수테스트
    - [X] 단위 테스트
    - [X] 코드 수정
- [X] 로그인 사용자의 경우 연령별 요금으로 계산 (청소년, 어린이)
  - [X] 인수테스트
  - [X] 단위테스트
  - [X] 코드 수정
- [ ] 리뷰 반영
  - [ ] 사용하지 않는 코드로 제거 (Path.java, getSections())
  - [ ] temp라는 prefix 삭제
  - [ ] 책임 연쇄 패턴, 요금 정책 순서 관리, 가중치 혹은 순서 변수 적용
  - [ ] 사용하지 않는 변수 제거
  - [ ] DiscountFareByAge 리팩토링
  - [ ] Null object pattern으로 구현
  - [ ] PathAcceptanceTest 검증을 전반적으로 하도록 수정
  - [ ] 어린이도 청소년도 아니지만 로그인은 되어있는 유저도 테스트케이스에 추가
  - [X] @ParameterizedTest의 name을 활용