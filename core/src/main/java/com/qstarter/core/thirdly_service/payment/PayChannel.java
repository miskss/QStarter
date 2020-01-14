package com.qstarter.core.thirdly_service.payment;

import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.thirdly_service.payment.ex.PayServiceException;

import java.math.BigDecimal;

/**
 * 在线支付
 *
 * @author peter
 * @version 1.0
 * @date 2019/12/03 13:20
 */
public interface PayChannel {

    /**
     * 统一下单接口
     *
     * @param billId 账单id（在系统中唯一）
     * @param price  账单的价格
     * @return prepare_id 预支付id
     * @throws SystemServiceException 将支付可能出现的异常转化成系统的异常
     */
    String ordering(String billId, BigDecimal price) throws PayServiceException;

    /**
     * 查询三方系统中账单的支付情况
     *
     * @param billId 账单id
     * @return 已支付为true，未支付为false
     */
    boolean queryBillPayStatus(String billId) throws PayServiceException;

    /**
     * 退款
     *
     * @param billId      账单id
     * @param refundPrice 退款金额
     * @return 成功 为true ，失败为false
     */
    boolean refund(String billId, BigDecimal refundPrice);


}
