package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.subway.fixture.LineFixture.신분당선;
import static nextstep.subway.fixture.LineFixture.이호선;
import static nextstep.subway.fixture.SectionFixture.강남_삼성_구간;
import static nextstep.subway.fixture.SectionFixture.강남_양재_구간;
import static nextstep.subway.fixture.SectionFixture.강남_역삼_구간;
import static nextstep.subway.fixture.SectionFixture.선릉_삼성_구간;
import static nextstep.subway.fixture.SectionFixture.역삼_삼성_구간;
import static nextstep.subway.fixture.SectionFixture.역삼_선릉_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.삼성역;
import static nextstep.subway.fixture.StationFixture.선릉역;
import static nextstep.subway.fixture.StationFixture.양재역;
import static nextstep.subway.fixture.StationFixture.역삼역;
import static nextstep.subway.unit.support.LineSupporter.구간_거리가_순서대로_저장되어_있다;
import static nextstep.subway.unit.support.LineSupporter.구간_거리와_소요시간_정보가_일치한다;
import static nextstep.subway.unit.support.LineSupporter.구간_소요시간이_순서대로_저장되어_있다;
import static nextstep.subway.unit.support.LineSupporter.구간의_상행역_하행역_정보가_일치한다;
import static nextstep.subway.unit.support.LineSupporter.구간이_N개_저장되어_있다;
import static nextstep.subway.unit.support.LineSupporter.역이_순서대로_정렬되어_있다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 노선 기능 단위 테스트")
class LineTest {

    private final Station 강남_역 = 강남역.엔티티_생성();
    private final Station 역삼_역 = 역삼역.엔티티_생성();
    private final Station 선릉_역 = 선릉역.엔티티_생성();
    private final Station 삼성_역 = 삼성역.엔티티_생성();
    private final Line 이호선라인 = 이호선.엔티티_생성();

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 구간_추가 {

        @DisplayName("구간을 순서대로 추가하면 구간 목록에 순서대로 저장된다")
        @Test
        void addSection() {
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());
            이호선라인.addSection(역삼_역, 삼성_역, 역삼_삼성_구간.구간_거리(), 역삼_삼성_구간.구간_소요시간());

            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 역삼_역, 삼성_역);
            구간_거리가_순서대로_저장되어_있다(이호선라인.getSections(), 강남_역삼_구간.구간_거리(), 역삼_삼성_구간.구간_거리());
            구간_소요시간이_순서대로_저장되어_있다(이호선라인.getSections(), 강남_역삼_구간.구간_소요시간(), 역삼_삼성_구간.구간_소요시간());
        }

        @DisplayName("상행 구간 기준(A-C)으로 신규 구간(A-B)을 목록 중간에 추가하면 구간 목록이 재정렬(A-B-C)된다")
        @Test
        void addSectionInMiddle() {
            이호선라인.addSection(강남_역, 삼성_역, 강남_삼성_구간.구간_거리(), 강남_삼성_구간.구간_소요시간());
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());

            구간이_N개_저장되어_있다(이호선라인, 2);
            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 역삼_역, 삼성_역);
            구간의_상행역_하행역_정보가_일치한다(이호선라인, 강남_역, 역삼_역);
            구간_거리와_소요시간_정보가_일치한다(이호선라인, 강남_역, 강남_역삼_구간);
        }

        @DisplayName("하행 구간 기준(A-C)으로 신규 구간(B-C)을 목록 중간에 추가하면 구간 목록이 재정렬(A-B-C)된다")
        @Test
        void addSectionInMiddle2() {
            이호선라인.addSection(강남_역, 삼성_역, 강남_삼성_구간.구간_거리(), 강남_삼성_구간.구간_소요시간());
            이호선라인.addSection(역삼_역, 삼성_역, 역삼_삼성_구간.구간_거리(), 역삼_삼성_구간.구간_소요시간());

            구간이_N개_저장되어_있다(이호선라인, 2);
            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 역삼_역, 삼성_역);
            구간의_상행역_하행역_정보가_일치한다(이호선라인, 역삼_역, 삼성_역);
            구간_거리와_소요시간_정보가_일치한다(이호선라인, 역삼_역, 역삼_삼성_구간);
        }

        @DisplayName("구간 목록의 최상단(B-C)에 신규 구간(A-B)을 추가하면 구간 목록이 재정렬(A-B-C)된다")
        @Test
        void addSectionInFront() {
            이호선라인.addSection(역삼_역, 삼성_역, 역삼_삼성_구간.구간_거리(), 역삼_삼성_구간.구간_소요시간());
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());

            구간이_N개_저장되어_있다(이호선라인, 2);
            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 역삼_역, 삼성_역);
            구간의_상행역_하행역_정보가_일치한다(이호선라인, 역삼_역, 삼성_역);
            구간_거리와_소요시간_정보가_일치한다(이호선라인, 역삼_역, 역삼_삼성_구간);
        }

        @DisplayName("구간 목록의 최하단(A-B)에 신규 구간(B-C)을 추가하면 구간 목록이 재정렬(A-B-C)된다")
        @Test
        void addSectionBehind() {
            이호선라인.addSection(역삼_역, 선릉_역, 역삼_선릉_구간.구간_거리(), 역삼_선릉_구간.구간_소요시간());
            이호선라인.addSection(선릉_역, 삼성_역, 선릉_삼성_구간.구간_거리(), 선릉_삼성_구간.구간_소요시간());

            구간이_N개_저장되어_있다(이호선라인, 2);
            역이_순서대로_정렬되어_있다(이호선라인, 역삼_역, 선릉_역, 삼성_역);
            구간의_상행역_하행역_정보가_일치한다(이호선라인, 역삼_역, 선릉_역);
            구간_거리와_소요시간_정보가_일치한다(이호선라인, 역삼_역, 역삼_선릉_구간);
        }

        @DisplayName("노선의 역 목록을 조회하면 역이 상행 종점역 기준으로 정렬되어 반환된다")
        @Test
        void getStations() {
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());
            이호선라인.addSection(강남_역, 삼성_역, 강남_삼성_구간.구간_거리(), 강남_삼성_구간.구간_소요시간());

            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 삼성_역, 역삼_역);
        }

        @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
        @Test
        void addSectionAlreadyIncluded() {
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());

            assertThatThrownBy(() -> 이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 구간_삭제 {

        @BeforeEach
        void setUp() {
            이호선라인.addSection(강남_역, 역삼_역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());
            이호선라인.addSection(역삼_역, 삼성_역, 역삼_삼성_구간.구간_거리(), 역삼_삼성_구간.구간_소요시간());
        }

        @DisplayName("노선의 하행 종점역을 삭제하면 구간 목록에서 조회되지 않는다")
        @Test
        void removeSection() {
            이호선라인.deleteSection(삼성_역);

            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 역삼_역);
        }

        @DisplayName("노선의 상행 종점역을 삭제하면 구간 목록에서 조회되지 않는다")
        @Test
        void removeSectionInFront() {
            이호선라인.deleteSection(강남_역);

            역이_순서대로_정렬되어_있다(이호선라인, 역삼_역, 삼성_역);
        }

        @DisplayName("노선의 중간역을 삭제하면 구간 목록이 재정렬되고 기존 구간 거리와 소요 시간이 합쳐진다")
        @Test
        void removeSectionInMiddle() {
            이호선라인.deleteSection(역삼_역);

            역이_순서대로_정렬되어_있다(이호선라인, 강남_역, 삼성_역);
            구간_거리와_소요시간_정보가_일치한다(이호선라인, 강남_역,
                    강남_역삼_구간.구간_거리() + 역삼_삼성_구간.구간_거리(),
                    강남_역삼_구간.구간_소요시간() + 역삼_삼성_구간.구간_소요시간()
            );
        }

        @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
        @Test
        void removeSectionNotEndOfList() {
            Line 신분당선라인 = 신분당선.엔티티_생성();
            Station 양재_역 = 양재역.엔티티_생성();

            신분당선라인.addSection(강남_역, 양재_역, 강남_양재_구간.구간_거리(), 강남_양재_구간.구간_소요시간());

            assertThatThrownBy(() -> 신분당선라인.deleteSection(양재_역))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
