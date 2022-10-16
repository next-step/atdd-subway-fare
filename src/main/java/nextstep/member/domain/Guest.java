package nextstep.member.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Guest extends Member {

    @Override
    public Integer getAge() {
        return Integer.MAX_VALUE;
    }

}
