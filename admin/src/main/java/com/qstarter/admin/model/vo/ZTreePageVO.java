package com.qstarter.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author peter
 * date: 2019-12-04 09:11
 **/
@Data
public class ZTreePageVO {

    private Integer id;

    private String name;

    @JsonProperty(value = "pId")
    private Integer pId;


    private Integer sortNum;


    public ZTreePageVO(Integer id, String name, Integer pId,Integer sortNum) {
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.sortNum = sortNum;
    }
}
