package com.qstarter.admin.model.dto;

        import io.swagger.annotations.ApiModel;
        import lombok.Data;
        import lombok.EqualsAndHashCode;

        import javax.validation.constraints.NotNull;

/**
 * @author peter
 * date: 2019-11-07 11:39
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ModifySysHtmlDTO extends SysHtmlDTO {

    @NotNull
    private Integer htmlId;
}
