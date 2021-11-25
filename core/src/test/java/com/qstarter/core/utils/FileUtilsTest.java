package com.qstarter.core.utils;


import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileUtilsTest {

    @Test
    public void deleteFile() throws IOException {
        FileUtils.deleteFile("/smart_file/img/1/1569222199374.jpg","/smart_file/img/1/1569220244725.jpg");
    }
}