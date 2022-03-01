package nextstep.subway.path.documentation;

import nextstep.subway.auth.AuthConfig;
import nextstep.subway.auth.token.JwtTokenProvider;
import nextstep.subway.auth.userdetails.UserDetailsService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.acceptance.PathUtils;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.ui.PathController;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.common.MockMvcDocumentationUtils.getDocumentRequest;
import static nextstep.subway.common.MockMvcDocumentationUtils.getDocumentResponse;
import static nextstep.subway.path.documentation.PathDocumentationUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = PathController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = AuthConfig.class
                )
        }
)
public class PathDocumentationMockMvc {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PathService pathService;

    @DisplayName("최소 경로를 조회한다.")
    @Test
    void findPath() throws Exception{
        // given
        when(pathService.findPath(anyInt(), anyLong(), anyLong(), any())).thenReturn(getPathResponse());

        // when
        ResultActions result = mockMvc.perform(get("/paths")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .param("source", 1L + "")
                .param("target", 2L + "")
                .param("type", PathType.DISTANCE.name())
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("path2",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    getRequestParameterSnippet(),
                                    getResponseFieldsSnippet()
                                )
                        );
    }
}
