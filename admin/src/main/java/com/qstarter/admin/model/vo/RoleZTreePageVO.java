package com.qstarter.admin.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author peter
 * date: 2019-12-04 10:23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleZTreePageVO extends ZTreePageVO {

    private boolean checked;

    public RoleZTreePageVO(Integer id, String name, Integer pId,Integer sortNum,boolean checked) {
        super(id, name, pId,sortNum);
        this.checked = checked;
    }
}
