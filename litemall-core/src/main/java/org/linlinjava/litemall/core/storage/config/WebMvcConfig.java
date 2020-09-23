package org.linlinjava.litemall.core.storage.config;

import org.linlinjava.litemall.core.storage.LocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private LocalStorage localStorage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String filePath = "file:\\"+System.getProperty("user.dir")+"\\"+localStorage.getStoragePath();
//        filePath = filePath.replace("\\","/");
//        registry.addResourceHandler("/file/**").addResourceLocations(filePath);
        registry.addResourceHandler("/file/**").addResourceLocations("file:/C:/Users/Admin/Desktop/mycode/litemall/storage/");

    }
}