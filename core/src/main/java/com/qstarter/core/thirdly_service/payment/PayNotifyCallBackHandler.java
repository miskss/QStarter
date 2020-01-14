package com.qstarter.core.thirdly_service.payment;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.qstarter.core.thirdly_service.payment.ex.PayCallbackHandlerFailException;

import java.util.Map;

/**
 * @author peter
 * create: 2020-01-13 10:25
 **/
public interface PayNotifyCallBackHandler {

    /**
     * 微信通知的结果处理
     * 处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
     *
     * @param result 账单的id{@link WxPayOrderNotifyResult#getOutTradeNo()}
     * @throws PayCallbackHandlerFailException 处理验证失败时
     */
    void handleWXCallback(WxPayOrderNotifyResult result) throws PayCallbackHandlerFailException;

    /**
     * 支付宝通知的结果处理
     * 自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
     * @param result map 中 账单id 对应的key="out_trade_no"
     * @throws PayCallbackHandlerFailException 处理验证失败时
     */
    void handleAlipayCallback(Map<String, String> result) throws PayCallbackHandlerFailException;
}
