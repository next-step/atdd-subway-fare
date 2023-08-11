package nextstep.subway.utils;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponses {
    사용자_12세("1", "access_token_1", "email1@email.com", 12),
    사용자_13세("2", "access_token_2", "email2@email.com", 13),
    사용자_14세("3", "access_token_3", "email3@email.com", 14),
    사용자_19세("4", "access_token_4", "email4@email.com", 19),
    사용자_20세("5", "access_token_5", "email5@email.com", 20);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponses(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponses findByCode(String code) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.code, code))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public static GithubResponses findByToken(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.accessToken, accessToken))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}

