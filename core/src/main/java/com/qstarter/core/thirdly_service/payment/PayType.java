package com.qstarter.core.thirdly_service.payment;

/**
 * 支付方式
 *
 * @author peter
 * @version 1.0
 * @date 2019/04/03 10:56
 */
public enum PayType {

    WXPAY(0, "微信", WxPay.INSTANCE),//微信
    ALIPAY(1, "支付宝", AliPay.INSTANCE);//支付宝

    private int value;

    private String msg;
    private PayChannel payChannel;

    PayType(final int value, final String msg, final PayChannel payChannel) {
        this.value = value;
        this.msg = msg;
        this.payChannel = payChannel;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public PayChannel getPayChannel() {
        return payChannel;
    }


}
