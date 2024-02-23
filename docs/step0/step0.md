## 테스트 실습
- [ ] test/resources/features/station.feature 파일의 지하철역을 생성한다 Scenario를 실행하기
  - 지하철역을 생성한다 Scenario를 실행하면 StationStepDef의 메서드가 하나씩 순서에 따라 실행됩니다.
  - 각각의 메서드가 수행되면서 남기는 로그를 확인합니다.
- [ ] 경로 조회 인수 테스트 cucumber 전환하기
  - PathAcceptanceTest의 경로 조회 인수 테스트를 cucumber 기반으로 작성합니다.
  - station.feature를 참고하여 진행하세요.
  - cucumber가 제공하는 기능을 적절히 활용해보세요.

## DataTable 사용
- 테이블 형태의 매개변수를 통해 값을 전달할 수 있습니다.
- 전달한 값은 DataTable 객체로 받거나 List, Map을 활용할 수 있습니다.
- 참고 - Cucumber Reference > Data Tables
- 참고 - List, Map외에 DTO 등 Java Object를 활용
- 참고 - Cucumber Data Tables

```
Given 지하철역들을 생성 요청하고
| name   |
| 교대역    |
| 강남역    |
...
```
 
```
Given("지하철역들을 생성 요청하고", (DataTable table) -> {
List<Map<String, String>> maps = table.asMaps();
maps.stream()
...
}
```

## 파라미터 사용
- 단계(Step) 작성 시 파라미터 타입을 지정하여 값을 전달 할 수 있습니다.
- 참고 - Step Definitions

```

Scenario: 지하철역을 생성한다.
When "교대역"과 "양재역"의 경로를 조회하면
...
Given("{string} 지하철역을 생성 요청하고", (String name) -> {
Map<String, String> params = new HashMap<>();
params.put("name", name);
```



## 공유 객체 활용
- 전 단계에서 응답 받은 정보를 이후 단계에서 활용하기 위해 공유 객체를 활용합니다.
- Spring의 IoC 컨테이너를 활용해도 좋고 PicoContainer를 활용해도 좋습니다.

```
@Profile("test")
@Component
public class AcceptanceContext {
public Map<String, Object> store = new HashMap<>();
public ExtractableResponse<Response> response;
}
```


## 데이터 초기화
- Before hook을 활용하여 데이터를 초기화 합니다.

```
public class BeforeStepDef implements En {
@Autowired
private DatabaseCleanup databaseCleanup;
@Autowired
private DataLoader dataLoader;

    public BeforeStepDef() {
        Before(() -> {
            databaseCleanup.execute();
            dataLoader.loadData();
        });
    }
}
```

## 기타
@Autowired 컴파일 에러

- 간혹 @Autowired에 컴파일 에러가 발생할 수 있음
- 테스트 동작에는 큰 문제가 아니니 진행하셔도 좋음
- 자세한 설명은 강의에서 진행 예정
