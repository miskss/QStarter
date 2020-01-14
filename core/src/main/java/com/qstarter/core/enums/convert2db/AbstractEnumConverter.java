package com.qstarter.core.enums.convert2db;

import javax.persistence.AttributeConverter;

/**
 * sample：
 *
 * public enum OrderType implements PersistEnum2DB<Integer> {
 *   ...
 *
 *   private Integer code;
 *   OrderType(int code) {
 *       this.code = code;
 *   }
 *
 *   public int getCode() {
 *       return code;
 *   }
 *   @Override
 *   public Integer getData() {
 *         return getCode();
 *   }
 *
 *   public static class Converter extends AbstractEnumConverter<OrderType,Integer> {
 *         public Converter() {
 *             super(OrderType.class);
 *         }
 *    }
 * }
 *
 * @Entity
 * public class Order {
 *      ...
 *
 *     @Convert(converter = OrderType.Converter.class)
 *     private OrderType orderType;
 *
 *     ...
 * }
 *
 * @param <ATTR> 实体类中枚举的类型，需实现{@link PersistEnum2DB} 接口
 * @param <DB>   保存到数据库的数据类型
 * @author peter
 * date: 2019-09-12 09:20
 **/
public abstract class AbstractEnumConverter<ATTR extends Enum<ATTR> & PersistEnum2DB<DB>, DB> implements AttributeConverter<ATTR, DB> {

    private final Class<ATTR> clazz;

    public AbstractEnumConverter(Class<ATTR> clazz) {
        this.clazz = clazz;
    }

    @Override
    public DB convertToDatabaseColumn(ATTR attribute) {
        return attribute != null ? attribute.getData() : null;
    }

    @Override
    public ATTR convertToEntityAttribute(DB dbData) {
        if (dbData == null) return null;

        ATTR[] enums = clazz.getEnumConstants();

        for (ATTR e : enums) {
            if (e.getData().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException("枚举转化异常。枚举【" + clazz.getSimpleName() + "】,数据库库中的值为：【" + dbData + "】");
    }

}
