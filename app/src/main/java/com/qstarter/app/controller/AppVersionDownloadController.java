package com.qstarter.app.controller;

import com.qstarter.app.entity.AppVersion;
import com.qstarter.app.model.vo.AppVersionVO;
import com.qstarter.app.service.AppVersionService;
import com.qstarter.app.utils.UrlUtils;
import com.qstarter.core.model.GenericMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author peter
 * date: 2019-12-20 16:22
 **/
@Api(tags = "【APP接口】APP的版本信息")
@Controller
@RequestMapping("/api/public/app")
public class AppVersionDownloadController {
    private AppVersionService appVersionService;

    public AppVersionDownloadController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @ApiOperation("获取最新的android apk")
    @GetMapping("/download/last-apk")
    public String downloadLastApk() {
        AppVersion androidLatestOne = appVersionService.findAndroidLatestOne();
        if (androidLatestOne == null) return "";
        return "redirect:" + UrlUtils.toHttpUrl(androidLatestOne.getAndroidApkPath());
    }

    @ApiOperation("获取android最新的版本信息")
    @GetMapping("/android")
    @ResponseBody
    public GenericMsg<AppVersionVO> android() {
        AppVersion androidLatestOne = appVersionService.findAndroidLatestOne();
        return GenericMsg.success(AppVersionVO.fromEntity(androidLatestOne));
    }

    @ApiOperation("获取ios最新的版本信息")
    @GetMapping("/ios")
    @ResponseBody
    public GenericMsg<AppVersionVO> ios() {
        AppVersion iosLatestOne = appVersionService.findIosLatestOne();
        return GenericMsg.success(AppVersionVO.fromEntity(iosLatestOne));
    }
}
