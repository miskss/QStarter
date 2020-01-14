package com.qstarter.core.thirdly_service.payment;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.qstarter.core.thirdly_service.payment.ex.PayServiceException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * <a>https://docs.open.alipay.com/204/105297/</a>
 * 阿里支付，单例
 *
 * @author peter
 * @version 1.0
 * @date 2019/04/04 08:51
 */
@Slf4j
public enum AliPay implements PayChannel {

    INSTANCE;


    /**
     * 阿里支付，各种服务调起的客户端。
     * 初始化之后就可以用alipayClient来调用具体的API了。
     * alipayClient只需要初始化一次，
     * 后续调用不同的API都可以使用同一个alipayClient对象
     */
    private AlipayClient alipayClient;

    {
        alipayClient = new DefaultAlipayClient(PayConfigConstant.AliConfig.URL,
                PayConfigConstant.AliConfig.appId, PayConfigConstant.AliConfig.APP_PRIVATE_KEY,
                PayConfigConstant.AliConfig.FORMAT, PayConfigConstant.AliConfig.CHARSET,
                PayConfigConstant.AliConfig.ALIPAY_PUBLIC_KEY, PayConfigConstant.AliConfig.SIGN_TYPE
        );
    }


    @Override
    public String ordering(String billId, BigDecimal price) throws PayServiceException {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.afterPay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(PayConfigConstant.BODY);
        model.setOutTradeNo(billId);
        model.setTotalAmount(price.toString());
        request.setBizModel(model);
        request.setNotifyUrl(PayConfigConstant.AliConfig.notifyUrl);
        try {
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(request);
            return alipayTradeAppPayResponse.getBody();
        } catch (AlipayApiException e) {
            log.error(e.getErrMsg(), e);
            throw new PayServiceException(e.getErrMsg(), e);
        }


    }

    @Override
    public boolean queryBillPayStatus(String billId) throws PayServiceException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(billId);
        request.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            log.error(e.getErrMsg(), e);
            throw new PayServiceException(e.getErrMsg(), e);
        }

    }

    @Override
    public boolean refund(String billId, BigDecimal refundPrice) {
        return false;
    }
}
