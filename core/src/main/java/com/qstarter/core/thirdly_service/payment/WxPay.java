package com.qstarter.core.thirdly_service.payment;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.thirdly_service.payment.ex.PayServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.RandomUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付通道，单例
 *
 * @author peter
 * @version 1.0
 * @date 2019/04/03 18:52
 */
@Slf4j
public enum WxPay implements PayChannel {

    INSTANCE;

    public static final String APPID = "appid";
    public static final String PARTNERID = "partnerid";
    public static final String PREPAYID = "prepayid";
    public static final String PACKAGE = "package";
    public static final String TIMESTAMP = "timestamp";
    public static final String SIGN = "sign";
    public static final String NONCESTR = "noncestr";

    public WxPayConfig getWxPayConfig() {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(PayConfigConstant.WXConfig.appId);
        config.setMchId(PayConfigConstant.WXConfig.mchId);
        config.setMchKey(PayConfigConstant.WXConfig.key);
        config.setNotifyUrl(PayConfigConstant.WXConfig.notifyUrl);
        return config;
    }

    WxPayService wxPayService;
    WxPayConfig config;

    {
        wxPayService = new WxPayServiceImpl();
        config = getWxPayConfig();
        wxPayService.setConfig(config);
    }

    @Override
    public String ordering(String billId, BigDecimal price) throws SystemServiceException {

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(PayConfigConstant.BODY);
        orderRequest.setOutTradeNo(billId);
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(price.toString()));
        orderRequest.setSpbillCreateIp("0.0.0.0");
        orderRequest.setTradeType(WxPayConstants.TradeType.APP);

        try {
            //请求类
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            return result.getPrepayId();

        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw new PayServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean queryBillPayStatus(String billId) throws SystemServiceException {
        try {
            WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, billId);
            String tradeState = wxPayOrderQueryResult.getTradeState();
            return WxPayConstants.WxpayTradeStatus.SUCCESS.equals(tradeState);

        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw new PayServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean refund(String billId, BigDecimal refundPrice) {
        return false;
    }


    public void closeBill(String billId) throws SystemServiceException {

        try {
            wxPayService.closeOrder(billId);
        } catch (WxPayException e) {
            log.error(e.getMessage(), e);
            throw new PayServiceException(e.getMessage(), e);
        }
    }

    /**
     * 返回给前台的参数
     * @param prepareId
     * @return
     */
    public Map<String, String> calcSign(String prepareId) {
        Map<String, String> needSignMap = new HashMap<>();

        needSignMap.put(APPID, config.getAppId());
        needSignMap.put(PARTNERID, config.getMchId());
        needSignMap.put(PREPAYID, prepareId);
        needSignMap.put(NONCESTR, RandomUtils.getRandomStr());
        needSignMap.put(PACKAGE, "Sign=WXPay");
        needSignMap.put(TIMESTAMP, String.valueOf(Instant.now().toEpochMilli() / 1000));
        String sign = SignUtils.createSign(needSignMap, WxPayConstants.SignType.MD5, config.getMchKey(), null);
        needSignMap.put(SIGN, sign);
        return needSignMap;
    }


}
