package com.qstarter.core.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具
 *
 * @author peter
 * date: 2019-09-11 17:37
 **/
@ApiModel("分页对象")
@Data
public class PageView<T> {
    @ApiModelProperty("总页数")
    private int pages;

    @ApiModelProperty("总条数")
    private long totalElements;

    @ApiModelProperty(value = "查询到的数据。如果没有数据则返回空集合")
    private List<T> elements;

    @ApiModelProperty(value = "当前页码")
    private int currentPage;//

    protected PageView() {
    }

    public static <T> PageView<T> empty() {
        PageView<T> tPageView = new PageView<>();

        tPageView.setElements(new ArrayList<>());

        return tPageView;

    }


    /**
     * 将查询的Page对象转换为PageView 视图
     *
     * @param pages  {@link Page}
     * @param mapper 转换器 example ：
     * @param <T>    转换后的数据类型
     * @param <E>    源数据类型
     * @return PageView<T>
     */
    public static <T, E> PageView<T> fromPage(Page<E> pages, Function<? super E, ? extends T> mapper) {
        if (pages == null) {
            throw new NullPointerException("pages must not be null");
        }
        PageView<T> pageView = new PageView<>();

        pageView.setPages(pages.getTotalPages());
        pageView.setTotalElements(pages.getTotalElements());
        pageView.setElements(
                pages.getContent()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toList())
        );
        pageView.setCurrentPage(pages.getNumber());
        return pageView;
    }

}
