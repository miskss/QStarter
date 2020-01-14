package com.qstarter.core.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 所有类的超类
 * 自动更新创建时间和更新时间
 *
 * @author peter
 * date: 2019-05-13 16:59
 **/
@MappedSuperclass
@ToString
@JsonIgnoreProperties(
        value = {"createTime", "updateTime"},
        allowGetters = true
)
@EntityListeners(value = AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;


    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateTime;


}
