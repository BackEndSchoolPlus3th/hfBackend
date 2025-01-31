package com.ll.hfback.global.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        AppConfig.objectMapper = objectMapper;
    }


    @Getter
    private static String siteFrontUrl;

    @Getter
    private static String siteBackUrl;

    @Getter
    private static String devFrontUrl;

    @Value("${custom.site.frontUrl}")
    public void setSiteFrontUrl(String siteFrontUrl) {
        AppConfig.siteFrontUrl = siteFrontUrl;
    }

    @Value("${custom.site.backUrl}")
    public void setSiteBackUrl(String siteBackUrl) {
        AppConfig.siteBackUrl = siteBackUrl;
    }

    @Value("${custom.dev.frontUrl}")
    public void setDevFrontUrl(String devFrontUrl) {
        AppConfig.devFrontUrl = devFrontUrl;
    }

    public static boolean isNotProd() {
        return true;
    }

}
