package com.qstarter.core.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-10-16 17:22
 **/
public class CollectionUtils {
    /**
     * 筛选出{@code src}集合中所有的 在{@code dest}集合中 不存在的元素
     *
     * @param src  源集合
     * @param dest 目标集合
     * @param <E>
     * @return 返回 {@code src}集合 不在{@code dest}集合中的元素 集合
     */
    public static <E> Collection<E> diffByDest(Collection<E> src, Collection<E> dest) {
        return src.stream()
                .filter(o -> !dest.contains(o))
                .collect(Collectors.toCollection((Supplier<Collection<E>>) () -> new ArrayList<>()));
    }

}
