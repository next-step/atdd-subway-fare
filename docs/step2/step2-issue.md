# 노선 찾기 역(Inverse) 탐색 문제 

노선에 따른 요금 추가는 최단 거리를 찾고 해당 거리가 밟은 노선을 찾아야 하는 문제이다.
즉, stations이 주어졌을 때 lines을 찾아야 한다. 따라서 stations -> sections -> lines 순으로 탐색을 찾아야 한다. 

어떻게, 그리고 어디서 해당 로직을 수행해야 할까? 

## 어떻게의 문제 

1. **먼저 stations을 찾아야 한다**:
    - 로직의 특성상 먼저 최단 거리를 찾은 다음에 line이 무엇인지를 거슬러 올라가야 했다. 
    - 최단 경로를 찾는 주된 로직은 `SimplePathService`의 `findShortestPath` 메소드에서 구현되어 있었다.
    - `DijkstraBasedShortestPathFinder`를 의존하며, 주어진 출발역과 도착역을 기반으로 최단 경로를 찾는다.
    - 찾은 Path 객체에는 `List<Station>`으로 역에 대한 정보가 담겨 있다.

2. **Path 객체에 필요한 정보 포함**:
    - 기존 코드에서는 노선 정보가 포함되지 않았으며, 단순히 경로의 역과 총 거리, 지속 시간만 계산했었다.
    - `Path` 객체가 노선 정보(`List<Line> linesTraversed`)를 포함하도록 변경. 해당 필드는 경로를 따라 이동할 때 어떤 노선을 거치는지 보여준다.

3. **탐색 로직 변경/추가**
   - service레이어에서 기존에는 sections를 파라미터로 제공했다면 이제는 lines를 제공하고, pathfinder에게 path에 대한 역할을 위임한다. sections를 넘기는 것이나 lines를 넘기는 것이나 책임의 관점에서 크게 다르지 않다고 보았다. 
   - 기존 로직은 stations에서 출발하여 관련 sections를 찾고 이를 기반으로 최단 경로를 계산했다. 새로운 구현에서는 이러한 접근 방식을 lines를 중심으로 재구성한다.
   - 새로운 접근 방식에서는 `SimplePathService`의 `findShortestPath` 및 `findMinimumDurationPath` 메소드가 수정되어, lines 정보를 직접 활용한다. 
```java
@Override
public PathInfo findShortestPath(Long source, Long target) {
	// ...
	List<Line> lines = lineResolver.fetchAll();
	return PathInfo.from(dijkstraBasedShortestPathFinder.findShortestPathByLines(sourceStation, targetStation, lines));
	}

@Override
public PathInfo findMinimumDurationPath(Long source, Long target) {
	// ...
	List<Line> lines = lineResolver.fetchAll();
	return PathInfo.from(dijkstraBaseMinimunDurationPathFinder.findShortestPathByLines(sourceStation, targetStation, lines));
	}
```
   - `DijkstraBasedShortestPathFinder`의 `findShortestPathByLines` 메소드는 주어진 lines를 기반으로 필요한 sections을 추출하여 그래프를 구성한다. 
   - 이렇게 구성된 그래프를 사용하여 최단 경로를 계산하고, 이 경로를 따라 거친 노선들을 결정한다.

    ```java
    @Override
    public Path findShortestPathByLines(Station sourceStation, Station targetStation, List<Line> lines) {
        List<Section> sections = fetchSections(sourceStation, targetStation, lines);
        // ...
        return Path.of(fetchStationsInPath(shortestPath), linesTraversed, calculateTotalDistance(graph, shortestPath), calculateTotalDuration(graph, shortestPath));
    }
    ```

   - 경로가 결정되면, 해당 경로에 포함된 모든 역들을 순회하면서 각 역을 거치는 동안 어떤 line들을 거쳤는지를 결정한다. 
   - 결정된 lines 집합은 `Path` 객체에 포함된다. 이렇게 하여, `Path` 객체는 이제 역들의 리스트 뿐만 아니라 거친 lines의 정보도 포함하게 된다.



## 어디서의 문제 


이와 같은 로직을 어디서 풀어낼까? 

### Facade에서 별도의 서비스를 호출하여 조합하기 

Facade 패턴은 여러 서비스 또는 로직을 하나의 인터페이스 뒤에 숨겨서 간단한 인터페이스를 클라이언트에게 제공하는 방식이다. 이를 통해 클라이언트는 복잡한 내부 로직에 대해 알 필요 없이 필요한 결과를 쉽게 얻을 수 있다.


#### 예시 코드 

```java
// Facade에서 LineService를 호출하여 조합
public PathResponse findPath(Long source, Long target, PathRequestType type) {
    // 기존의 PathService와 FareCalculationService를 사용하여 경로 정보와 요금 정보를 가져온다
    PathInfo pathInfo = getPathInfo(source, target, type);

    // LineService를 호출하여 line 정보를 가져옴 
    List<LineInfo> lineInfos = lineService.getLinesInfoByPath(pathInfo.getStations());

	// 가져온 line 정보를 기반으로 fare 정보 계산 
    FareInfo fareInfo = fareCalculationService.calculate(pathInfo, lineInfos);
	
    // PathResponse를 조합하여 반환
    return PathResponse.of(pathInfo, fareInfo, lineInfos);
}
```

Facade 패턴을 사용하면 책임이 명확하게 분리되어 경계가 명확해진다는 점에서 장점이 있다고 생각한다. 

### PathFinder에게 Line에 대한 책임까지 위임하기 (현재의 방식)

`Path`에 대한 도메인 바운더리를 어떻게 정의할 것이냐에 대해 어떻게 보느냐에 따라 관점이 달라진다. 직관적으로 최단거리라 할 때 사람의 인지작용을 생각해보면 노선 정보를 자연스럽게 이해한다. 이러한 점을 고려하여 Path 탐색에 노선 정보 탐색이 포함되는 과정이라고 본 것이다.

즉, Path 객체에 확장된 책임을 요구한다. 

따라서 `DijkstraBasedShortestPathFinder`는 stations에서 출발하여 lines를 찾는 책임을 공유한다.


이러한 방식은 최단 경로와 노선 정보를 한 곳에서 처리함으로써, 로직의 일관성을 유지할 수 있다는 점이 장점이다. 

PathFinder가 경로 탐색과 노선 정보 처리를 함께 담당하는 현재의 방식은 효율적인 정보 제공과 로직의 일관성 측면에서 강점을 갖는다. 
그러나 이로 인해 책임의 범위가 확대되고, 시스템의 복잡도가 증가하는 측면도 고려해야 한다.



