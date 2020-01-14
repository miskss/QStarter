package com.qstarter.admin.config;

import com.qstarter.core.config.FileProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author peter
 * date: 2019-09-05 11:03
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private FileProperties fileProperties;

    public WebConfig(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*   swagger */
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        String imgURL = fileProperties.getRoot() + "/**";
        String imgLocal2 = "file:" + fileProperties.getRoot() + "/";



        registry.addResourceHandler(imgURL)
                .addResourceLocations(imgLocal2);


    }


}
