package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    Station 강남역;
    Station 역삼역;
    Section 강남역_역삼역_구간;
    Sections 구간들;
    Line 신분당선;
    Path path_비로그인;
    Path path_로그인_8세;
    Path path_로그인_18세;
    Path path_로그인_20세;

    Long 기본_노선_추가요금 = 0L;
    Long 총_거리 = 10L;
    Long 총_시간 = 5L;
    Long 총_비용 = null;

    Member 로그인_사용자_비할인대상;
    Member 로그인_사용자_8세;
    Member 로그인_사용자_18세;
    Member 로그인_사용자_20세;

    @BeforeEach
    public void setup() {
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");

        강남역_역삼역_구간 = Section.of(강남역, 역삼역, 10L, 5L);
        구간들 = new Sections(Collections.singletonList(강남역_역삼역_구간));
        신분당선 = Line.of(1L, "신분당선", "red", 15L, 구간들, 기본_노선_추가요금);

        로그인_사용자_비할인대상 = Member.of(1L, "test@test.com", "password", 20);
        로그인_사용자_8세 = Member.of(1L, "test@test.com", "password", 8);
        로그인_사용자_18세 = Member.of(1L, "test@test.com", "password", 18);
        로그인_사용자_20세 = Member.of(1L, "test@test.com", "password", 20);

        path_비로그인 = Path.of(null, List.of(신분당선), List.of(강남역, 역삼역), 구간들);
        path_로그인_8세 = Path.of(로그인_사용자_8세, List.of(신분당선), List.of(강남역, 역삼역), 구간들);
        path_로그인_18세 = Path.of(로그인_사용자_18세, List.of(신분당선), List.of(강남역, 역삼역), 구간들);
        path_로그인_20세 = Path.of(로그인_사용자_20세, List.of(신분당선), List.of(강남역, 역삼역), 구간들);
    }

    @DisplayName("getVertexList와 getWeight의 정상 동작을 확인한다.")
    @Test
    public void getVertexList_getWeight() {
        // then
        assertAll(
                () -> assertThat(List.of(강남역, 역삼역)).isEqualTo(path_비로그인.getStations()),
                () -> assertThat(총_거리).isEqualTo(path_비로그인.getTotalDistance()),
                () -> assertThat(총_시간).isEqualTo(path_비로그인.getTotalDuration()),
                () -> assertThat(총_비용).isEqualTo(path_비로그인.getTotalPrice())
        );
    }

    @DisplayName("[createPathResponse] pathResponse를 생성한다.")
    @Test
    void createPathResponse_success() {
        // when
        var pathResponse = path_비로그인.createPathResponse();

        // then
        assertAll(
                () -> assertNotNull(pathResponse),
                () -> assertThat(총_거리).isEqualTo(pathResponse.getTotalDistance()),
                () -> assertThat(총_시간).isEqualTo(pathResponse.getTotalDuration()),
                () -> assertThat(총_비용).isEqualTo(pathResponse.getTotalPrice()),
                () -> assertThat(List.of(
                        StationResponse.of(강남역.getId(), 강남역.getName()),
                        StationResponse.of(역삼역.getId(), 역삼역.getName())
                )).isEqualTo(pathResponse.getStationResponses())
        );
    }

    @DisplayName("[createPathResponse] path의 stationList가 비어 있으면 예외가 발생한다.")
    @Test
    void createPathResponse_fail1() {
        // given
        var path = Path.of(로그인_사용자_비할인대상, List.of(신분당선), List.of(), 구간들);

        // when & then
        assertThrows(PathException.class, () -> path.createPathResponse())
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }
}

