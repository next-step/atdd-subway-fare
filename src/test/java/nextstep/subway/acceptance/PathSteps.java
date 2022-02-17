package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.entity.ContentType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static ExtractableResponse<Response> 최단_경로_조회(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(String.valueOf(ContentType.APPLICATION_JSON))
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로_조회(Long source, Long target, RequestSpecification spec,
                                                                ParameterDescriptor[] parameterDescriptors,
                                                                FieldDescriptor[] fieldDescriptors,
                                                                String type) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestParameters(parameterDescriptors),
                        relaxedResponseFields(fieldDescriptors)
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths/durationTest")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 최소_시간_경로_조회(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(String.valueOf(ContentType.APPLICATION_JSON))
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DURATION")
                .when().get("/paths/durationTest")
                .then().log().all().extract();
    }
}
