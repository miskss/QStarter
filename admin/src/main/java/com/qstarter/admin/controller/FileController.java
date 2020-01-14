package com.qstarter.admin.controller;

import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.service.FileService;
import com.qstarter.security.utils.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author peter
 * date: 2019-09-12 16:10
 **/
@Api(tags = "【后台网页】文件上传")
@RestController
@RequestMapping("/api/web/file")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ApiOperation(value = "图片上传（from-data），支持单张或多张图片，返回上传成功后的图片地址")
    @PostMapping("/img")
    public GenericMsg<List<String>> post(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
        Long userId = ContextHolder.getUserId();
        return GenericMsg.success(fileService.uploadImg(userId,multiFileMap.values().stream().flatMap(Collection::stream).toArray(MultipartFile[]::new)));
    }


}
