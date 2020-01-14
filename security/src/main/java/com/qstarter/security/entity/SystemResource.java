package com.qstarter.security.entity;

import com.qstarter.core.entity.BaseEntity;
import com.qstarter.security.enums.OperationEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 系统资源表
 * RoleInSystem n...n SystemResource
 *
 * @author peter
 * date: 2019-09-04 16:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class SystemResource extends BaseEntity {

    private static final long serialVersionUID = 4133268899490752978L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName;

    private String resourceUrl;

    @Enumerated(value = EnumType.STRING)
    private OperationEnum operation;


}
