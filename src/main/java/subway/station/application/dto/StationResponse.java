package subway.station.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationResponse {

    private Long id;

    private String name;

    public static StationResponse from(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

    public static List<StationResponse> from(List<Section> sections) {
        List<Station> stations = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
        return stations.stream()
                .map(station -> StationResponse.builder()
                        .id(station.getId())
                        .name(station.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
