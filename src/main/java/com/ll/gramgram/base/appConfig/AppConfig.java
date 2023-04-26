package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Getter
    private static long likeablePersonFromMax;

    @Value("${constant.max-likeable-person}")
    public void setLikeablePersonFromMax(long likeablePersonFromMax) {
        AppConfig.likeablePersonFromMax = likeablePersonFromMax;
    }
}