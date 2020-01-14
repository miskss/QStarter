package com.qstarter.core.thirdly_service.payment.vo;

import com.qstarter.core.thirdly_service.payment.WxPay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author peter
 * @version 1.0
 * @date 2019/04/04 09:41
 */
@Data
@ApiModel
public class PayChannelVO {
    @ApiModelProperty(value = "订单id", dataType = "String")
    private String orderId;
    @ApiModelProperty(value = "账单id", dataType = "String")
    private String outTradeNo;
    @ApiModelProperty(value = "支付类型", dataType = "int")
    private Integer payType;
    @ApiModelProperty(value = "订单的价格", dataType = "BigDecimal")
    private BigDecimal orderPrice;
    //调起微信支付相关的信息
    @ApiModelProperty(value = "微信支付的相关信息，与aliPayConf属性互斥")
    private WxPayConf wxPayConf;
    @ApiModelProperty(value = "阿里支付的相关信息，与wxPayConf属性互斥")
    private AliPayConf aliPayConf;

    /**
     * 微信
     *
     * @author peter
     * @version 1.0
     * @date 2019/04/04 10:57
     */
    @Data
    @ApiModel()
    public static class WxPayConf {

        @ApiModelProperty(value = "appid")
        private String appId;//appid
        @ApiModelProperty(value = "商户id")
        private String partnerId;//商户id
        @ApiModelProperty(value = "随机字符串")
        private String nonceStr;//随机字符串
        @ApiModelProperty(value = "时间戳")
        private String timeStr;//时间戳
        @ApiModelProperty(value = "暂填写固定值Sign=WXPay")
        private String extra;//暂填写固定值Sign=WXPay
        @ApiModelProperty(value = "sign签名")
        private String sign;
        @ApiModelProperty(value = "支付调起信息")
        private String paymentStr;

        /**
         * 从map中提取值来生成对象
         *
         * @param map 从 {@link WxPay#calcSign(String)} 方法提取出来
         * @return
         */
        public static WxPayConf fromMap(Map<String, String> map) {
            WxPayConf wxPayConf = new WxPayConf();
            wxPayConf.setAppId(map.get(WxPay.APPID));
            wxPayConf.setPartnerId(map.get(WxPay.PARTNERID));
            wxPayConf.setPaymentStr(map.get(WxPay.PREPAYID));
            wxPayConf.setNonceStr(map.get(WxPay.NONCESTR));
            wxPayConf.setExtra(map.get(WxPay.PACKAGE));
            wxPayConf.setSign(map.get(WxPay.SIGN));
            wxPayConf.setTimeStr(map.get(WxPay.TIMESTAMP));

            return wxPayConf;
        }

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel()
    public static class AliPayConf {
        @ApiModelProperty(value = "支付调起信息")
        private String paymentStr;
    }
}
