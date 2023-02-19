package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathSteps {

    public static RequestSpecification given(final RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    public static RequestParametersSnippet getPathRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("The source is station for ascending"),
                parameterWithName("target").description("The target is station for descending"));
    }

    public static ResponseFieldsSnippet getPathResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description(""),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("Id of the station"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("Name of the station"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("The distance is between up and down"));
    }
}
