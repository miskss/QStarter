package com.qstarter.core.thirdly_service.payment.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qstarter.core.thirdly_service.payment.PayConfigConstant;
import com.qstarter.core.thirdly_service.payment.PayNotifyCallBackHandler;
import com.qstarter.core.thirdly_service.payment.WxPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付回调通知
 * 需要实现{@link PayNotifyCallBackHandler}
 *
 * @author peter
 * @version 1.0
 * @date 2019/04/04 08:58
 */
@RestController
@RequestMapping("/api/public/pay/notify")
@Slf4j
@ConditionalOnBean(PayNotifyCallBackHandler.class)
public class PayNotifyCallBackController {

    private static final String FAIL = "fail";
    private static final String SUCCESS = "success";

    private final PayNotifyCallBackHandler handler;
    public PayNotifyCallBackController(PayNotifyCallBackHandler handler) {
        this.handler = handler;
    }


    @RequestMapping("/wx")
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {

            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(WxPay.INSTANCE.getWxPayConfig());

            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            handler.handleWXCallback(result);

            return WxPayNotifyResponse.success("处理成功!");

        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因：【{}】", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

    }


    @PostMapping("/ali")
    public String aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
//            //乱码解决，这段代码在出现乱码时使用。
//            try {
//                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                log.error(e.getMessage(),e);
//            }
            params.put(name, valueStr);
        }

        try {
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            boolean flag = AlipaySignature.rsaCheckV1(params, PayConfigConstant.AliConfig.ALIPAY_PUBLIC_KEY, PayConfigConstant.AliConfig.CHARSET, "RSA2");
            if (!flag) {
                log.error("支付宝验签失败,信息【{}】", params);
                return FAIL;
            }
            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            handler.handleAlipayCallback(params);
        } catch (AlipayApiException e) {
            log.error(e.getErrCode() + e.getErrMsg(), e);
            return FAIL;
        } catch (Exception e) {
            return FAIL;
        }

        return SUCCESS;
    }


}
