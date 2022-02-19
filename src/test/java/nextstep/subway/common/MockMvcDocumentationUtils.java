package nextstep.subway.common;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class MockMvcDocumentationUtils {

    private MockMvcDocumentationUtils() {
    }

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            modifyUris() // 문서상 uri를 기본값인 http://localhost:8080에서 https://docs.api.com으로 변경하기 위함
                    .scheme("https")
                    .host("docs.api.com")
                    .removePort(),
                prettyPrint()
        );
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
