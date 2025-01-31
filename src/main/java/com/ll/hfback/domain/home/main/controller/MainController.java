package com.ll.hfback.domain.home.main.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class MainController {

    @Value("${custom.site.backUrl}")
    private String backUrl;

    @Value("${custom.site.frontUrl}")
    private String frontUrl;

    @GetMapping("/api")
    public String mainPage(){

        System.out.println("backUrl = " + backUrl);
        System.out.println("frontUrl = " + frontUrl);

        return "Welcome to Main Page";
    }


    @GetMapping("/api/session")
    @ResponseBody
    public String session(HttpSession session) {
        String sessionDump = Stream.iterate(
                session.getAttributeNames().asIterator(),
                Iterator::hasNext,
                it -> it
            ).flatMap(it -> Stream.of(it.next()))
            .map(attributeName -> {
                Object attributeValue = session.getAttribute(attributeName);
                return attributeName + " = " + attributeValue;
            })
            .collect(Collectors.joining("\n", "Session Attributes:\n", ""));

        // 완성된 세션 정보 반환
        return sessionDump;
    }
}
