package nextstep.support.entity;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class OperateBaseEntity extends RegisterBaseEntity {

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
