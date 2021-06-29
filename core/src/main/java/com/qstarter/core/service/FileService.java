package com.qstarter.core.service;

import com.qstarter.core.config.FileProperties;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.event.DeleteFileEvent;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件的相关服务
 *
 * @author peter
 * date: 2019-09-12 15:27
 **/
@Component
@Slf4j
public class FileService {

    private final FileProperties fileProperties;
    private static final String APK_TYPE = ".apk";
    private static final long MAX_SIZE = 100 * 1024 * 1024;

    public FileService(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }


    public String uploadApk(MultipartFile apk) {
        String originalFilename = apk.getOriginalFilename();
        assert originalFilename != null;

        String suffix = FileUtils.getPointSuffix(originalFilename);

        if (!APK_TYPE.equals(suffix)) {
            throw new SystemServiceException(ErrorMessageEnum.FILE_TYPE_INCORRECT, "只能上传apk类型的文件");
        }

        long size = apk.getSize();

        if (size > MAX_SIZE) {
            throw new SystemServiceException(ErrorMessageEnum.FILE_SIZE_OVERSIZE, "文件必须小于" + (MAX_SIZE / 1024 * 1024) + "MB");
        }
        String fileName = Instant.now().toEpochMilli() + FileUtils.getPointSuffix(originalFilename);

        try {
            return FileUtils.uploadFile(apk, fileProperties.getApkPath(), fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new SystemServiceException(ErrorMessageEnum.IMAGE_UPLOAD_FAIL, "apk上传失败");
        }

    }

    /**
     * 上传图片文件
     * 图片路径 {@link FileProperties#getImgPath()} +"/" +{@code userId}+"/"+{@code fileName}
     *
     * @param userId 用户id
     * @param files  文件
     * @return List<String> 上传成功的文件地址
     */
    public List<String> uploadImg(Long userId, MultipartFile... files) {

        return Arrays.stream(files).map(file -> {

            String originalFilename = file.getOriginalFilename();

            String rootPath = fileProperties.getImgPath() + "/" + userId;
            String fileName = Instant.now().toEpochMilli() + FileUtils.getPointSuffix(originalFilename);

            boolean image = FileUtils.isImage(file);

            if (!image)
                throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "只能上传图片");

            try {
                return FileUtils.uploadFile(file, rootPath, fileName);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SystemServiceException(ErrorMessageEnum.IMAGE_UPLOAD_FAIL);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 上传图片文件
     * 图片路径 {@link FileProperties#getImgPath()} +"/" +{@code userId}+"/"+{@code fileName}
     *
     * @param userId 用户id
     * @param files  文件
     * @return List<String> 上传成功的文件地址
     */
    public List<String> uploadImgCompress(Long userId, MultipartFile... files) {
        return Arrays.stream(files).map(file -> {
            String originalFilename = file.getOriginalFilename();

            String path = fileProperties.getImgPath() + "/" + userId + "/" + System.currentTimeMillis() + FileUtils.getPointSuffix(originalFilename);
            boolean image = FileUtils.isImage(file);

            if (!image)
                throw new SystemServiceException(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "只能上传图片");

            //对图片进行压缩
            try {
                File img = FileUtils.getFile(path);
                Thumbnails.of(file.getInputStream())
                        .scale(1f)
                        .toFile(img);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SystemServiceException(ErrorMessageEnum.IMAGE_UPLOAD_FAIL);
            }
            return path;
        }).collect(Collectors.toList());
    }


    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void listenDeleteFileEvent(DeleteFileEvent event) {
        Path data = event.getData();
        try {
            FileUtils.deleteFile(data);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
