package com.qstarter.core.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author peter
 * date: 2019-09-12 15:49
 **/
public final class FileUtils {

    /**
     * 上传文件
     *
     * @param file     文件
     * @param rootPath 目录
     * @param fileName 文件名
     * @return 文件地址
     * @throws IOException 异常
     */
    public static String uploadFile(MultipartFile file, String rootPath, String fileName) throws IOException {
        return uploadFile(file.getInputStream(), rootPath, fileName);
    }

    /**
     * 上传文件
     *
     * @param inputStream 文件
     * @param rootPath    目录
     * @param fileName    文件名
     * @return 文件地址
     * @throws IOException 异常
     */
    public static String uploadFile(InputStream inputStream, String rootPath, String fileName) throws IOException {
        Path directory = Paths.get(rootPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        Files.copy(inputStream, directory.resolve(fileName));
        return rootPath + "/" + fileName;
    }

    public static byte[] readFile(String rootPath, String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(rootPath).resolve(fileName));
    }


    public static File getFile(String path) throws IOException {
        Path filePath = Paths.get(path);

        Path parent = filePath.getParent();

        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        return filePath.toFile();
    }

    //
    public static String getPointSuffix(String originalName) {
        return "." + getSuffix(originalName);
    }


    //获取后缀名
    public static String getSuffix(String originalName) {
        int lastIndexOf = originalName.lastIndexOf(".");
        return (lastIndexOf == -1) ? "" : originalName.substring(lastIndexOf + 1);
    }


    public static boolean isImage(MultipartFile file) {
        try {
            return ImageIO.read(file.getInputStream()) != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean deleteFile(String... filePaths) throws IOException {
        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        }
        return true;
    }

    public static boolean deleteFile(Path... paths) throws IOException {
        for (Path path : paths) {
            Files.deleteIfExists(path);
        }
        return true;
    }

}
