package com.ll.hfback.domain.festival.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {
    private String festivalId;
    private String festivalName;
    private String festivalArea;
    private String festivalStartDate;
    private String festivalEndDate;
    private String festivalUrl;
}
