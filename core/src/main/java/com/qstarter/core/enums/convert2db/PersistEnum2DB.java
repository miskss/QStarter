package com.qstarter.core.enums.convert2db;

/**
 * @param <DB> : 保存到数据库的数据类型
 * @author peter
 * date: 2019-09-12 09:19
 **/
public interface PersistEnum2DB<DB> {
    DB getData();
}
