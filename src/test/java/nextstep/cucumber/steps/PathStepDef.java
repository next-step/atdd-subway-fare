package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.line.dto.LineResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.path.dto.PathResponse;
import nextstep.section.dto.SectionRequest;
import nextstep.section.dto.SectionResponse;
import nextstep.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.utils.step.AuthStep.로그인_후_토큰_발급;
import static nextstep.utils.step.LineStep.지하철_노선_생성;
import static nextstep.utils.step.MemberStep.멤버_생성;
import static nextstep.utils.step.PathStep.*;
import static nextstep.utils.step.SectionStep.지하철_구간_등록;
import static nextstep.utils.step.StationStep.지하철_역_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathStepDef implements En {
    private final String BEARER_ = "Bearer ";

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                StationResponse stationResponse = 지하철_역_등록(params.get("name"));

                context.store.put(stationResponse.getName(), stationResponse);
            }
        });

        And("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                StationResponse upStation = (StationResponse) context.store.get(params.get("upStation"));
                StationResponse downStation = (StationResponse) context.store.get(params.get("downStation"));
                Long additionalFare = Long.valueOf(params.get("additionalFare"));

                LineResponse lineResponse = 지하철_노선_생성(params.get("name"), params.get("color"), upStation.getId(), downStation.getId(), Long.valueOf(params.get("distance")), Long.valueOf(params.get("duration")), additionalFare);

                context.store.put(params.get("name"), lineResponse);
            }
        });

        And("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                LineResponse lineResponse = (LineResponse) context.store.get(params.get("lineName"));
                StationResponse upStation = (StationResponse) context.store.get(params.get("upStation"));
                StationResponse downStation = (StationResponse) context.store.get(params.get("downStation"));
                Long distance = Long.valueOf(params.get("distance"));
                Long duration = Long.valueOf(params.get("duration"));

                SectionResponse sectionResponse = 지하철_구간_등록(lineResponse.getId(), SectionRequest.of(upStation.getId(), downStation.getId(), distance, duration));
            }
        });

        And("여러 사용자를 생성한다", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                MemberRequest memberRequest = MemberRequest.of(params.get("email"), params.get("password"), Integer.parseInt(params.get("age")));
                멤버_생성(memberRequest);
                context.store.put(params.get("email"), params.get("email"));
            }
        });

        When("{string} 사용자로 로그인한다", (String user2) -> {
            String accessToken = 로그인_후_토큰_발급(user2, "password");
            context.store.put(user2, accessToken);
        });

        When("{string} 사용자로 로그인한 상태에서 {string}과 {string}의 경로를 최단 거리 기준으로 조회하면", (String email, String source, String target) -> {
            String accessToken = (String) context.store.get(email);

            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();

            PathResponse pathResponse = 경로_조회_거리_헤더_설정(sourceId, targetId, accessToken);
            context.store.put("path", pathResponse);
        });


        When("{string}과 {string}의 경로를 최단 거리 기준으로 조회하면", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();

            PathResponse pathResponse = 경로_조회_거리(sourceId, targetId);
            context.store.put("path", pathResponse);
        });

        Then("최단 거리 {string} 을 기준으로 {string} 경로가 조회된다", (String expectedDistance, String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            PathResponse pathResponse = (PathResponse) context.store.get("path");
            List<String> actualPath = pathResponse.getStationResponses().stream().map(value -> value.getName()).collect(Collectors.toList());

            assertAll(
                    () -> assertThat(actualPath).containsExactly(split.toArray(new String[0])),
                    () -> assertThat(pathResponse.getTotalDistance()).isEqualTo(Long.valueOf(expectedDistance))
            );
        });

        When("{string}과 {string}의 경로를 최소 시간 기준으로 경로 조회하면", (String source, String target) -> {
            StationResponse sourceStation = (StationResponse) context.store.get(source);
            StationResponse targetStation = (StationResponse) context.store.get(target);

            PathResponse pathResponse = 경로_조회_시간(sourceStation.getId(), targetStation.getId());
            context.store.put("path", pathResponse);
        });

        Then("최소 시간 {string} 시간 기준으로 {string} 경로가 조회된다", (String expectedDuration, String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            PathResponse pathResponse = (PathResponse) context.store.get("path");
            List<String> actualPath = pathResponse.getStationResponses().stream().map(value -> value.getName()).collect(Collectors.toList());

            assertAll(
                    () -> assertThat(actualPath).containsExactly(split.toArray(new String[0])),
                    () -> assertThat(pathResponse.getTotalDuration()).isEqualTo(Long.valueOf(expectedDuration))
            );
        });

        And("총 거리 {string} 을 함께 응답한다", (String expectedDistance) -> {
            PathResponse pathResponse = (PathResponse) context.store.get("path");

            assertAll(
                    () -> assertThat(pathResponse.getTotalDistance()).isEqualTo(Long.valueOf(expectedDistance))
            );
        });

        And("총 소요 시간 {string} 을 함께 응답한다", (String totalDuration) -> {
            PathResponse pathResponse = (PathResponse) context.store.get("path");

            assertAll(
                    () -> assertThat(pathResponse.getTotalDuration()).isEqualTo(Long.valueOf(totalDuration))
            );
        });

        And("지하철 이용 요금 {string} 도 함께 응답한다", (String totalPrice) -> {
            PathResponse pathResponse = (PathResponse) context.store.get("path");

            assertAll(
                    () -> assertThat(pathResponse.getTotalPrice()).isEqualTo(Long.valueOf(totalPrice))
            );
        });
    }
}

