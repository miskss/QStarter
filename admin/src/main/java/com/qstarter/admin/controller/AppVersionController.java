package com.qstarter.admin.controller;

import com.qstarter.app.model.dto.AppVersionDTO;
import com.qstarter.app.service.AppVersionService;
import com.qstarter.core.enums.DeviceTypeEnum;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.service.FileService;
import com.qstarter.core.utils.EnumUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @author peter
 * date: 2019-12-20 09:45
 **/
@RestController
@RequestMapping("/api/web/app-version")
public class AppVersionController {
    private FileService fileService;
    private AppVersionService appVersionService;
    public AppVersionController(FileService fileService, AppVersionService appVersionService) {
        this.fileService = fileService;
        this.appVersionService = appVersionService;
    }

    @PostMapping("/apk")
    public GenericMsg<String> uploadApk(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
        MultipartFile file = multiFileMap.values().stream().flatMap(Collection::stream).findFirst().orElse(null);
        if (file ==null){
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION,"apk文件不存在");
        }
        String apk = fileService.uploadApk(file);
        return GenericMsg.success(apk);
    }

    @PostMapping
    @PreAuthorize("hasPermission('','')")
    public GenericMsg addAppVersion(@Valid @RequestBody AppVersionDTO dto){
        @NotBlank @NotNull String device = dto.getDevice();
        DeviceTypeEnum deviceTypeEnum = EnumUtils.convertToEnum(DeviceTypeEnum.class, device);
        if (deviceTypeEnum == DeviceTypeEnum.ANDROID){
            appVersionService.uploadAndroid(dto);
        }else if (deviceTypeEnum == DeviceTypeEnum.IOS){
            appVersionService.uploadIos(dto);
        }else {
            throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION,"类型不正确");
        }
        return GenericMsg.success();
    }
    @GetMapping("/android")
    public GenericMsg appVersionAndroid(){
        return GenericMsg.success(appVersionService.findAndroidLatestOne());
    }
    @GetMapping("/ios")
    public GenericMsg appVersionIos(){
        return GenericMsg.success(appVersionService.findIosLatestOne());
    }

}
