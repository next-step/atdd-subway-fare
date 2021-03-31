package nextstep.subway.line.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.BaseDocumentSteps;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.line.acceptance.documentation.LineDocumentation.initDocumentCommonResponseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class LineFindDocumentation extends BaseDocumentSteps {

    public LineFindDocumentation(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public PathParametersSnippet initDocumentRequestPathVariable() {
        return pathParameters(
                parameterWithName("lineId").description("지하철 노선 ID")
        );
    }

    @Override
    public RequestParametersSnippet initDocumentRequestParameters() {
        return requestParameters();
    }

    @Override
    public RequestFieldsSnippet initDocumentRequestBody() {
        return requestFields();
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
        return initDocumentCommonResponseBody();
    }
}
