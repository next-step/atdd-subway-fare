package nextstep.documentation;

import java.util.Collections;
import java.util.Map;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import nextstep.subway.maps.map.domain.PathType;

public class PathTypesSnippet extends TemplatedSnippet {
    public static final String SNIPPET_NAME = "path-types";

    public PathTypesSnippet(Map<String, Object> attributes) {
        super(SNIPPET_NAME, attributes);
    }

    public PathTypesSnippet() {
        super(SNIPPET_NAME, null);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        return Collections.singletonMap("pathTypes", PathType.values());
    }
}
