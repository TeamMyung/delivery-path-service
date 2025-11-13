package com.sparta.deliverypathservice.global.entity;


import com.sparta.deliverypathservice.global.config.AuditingConfig;
import com.sparta.deliverypathservice.global.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

//    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

//    @LastModifiedBy
    private Long updatedBy;

    private Long deletedBy;

    public void delete(Long userId) {
        deletedAt = LocalDateTime.now();
        deletedBy = userId;
    }

//    public void delete() {
//        AuditorAware<Long> auditorAware = new AuditingConfig.AuditorAwareImpl();
//
//        deletedAt = LocalDateTime.now();
//        deletedBy = auditorAware.getCurrentAuditor().orElse(null);
//    }
}
