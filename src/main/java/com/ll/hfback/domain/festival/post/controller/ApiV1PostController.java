package com.ll.hfback.domain.festival.post.controller;

import com.ll.hfback.domain.festival.post.dto.DetailPostDto;
import com.ll.hfback.domain.festival.post.dto.PostDto;
import com.ll.hfback.domain.festival.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    // 모든 게시글 조회
    @GetMapping("/all")
    public Page<PostDto> getAllPosts(@ParameterObject Pageable pageable) {
        Page<PostDto> posts = postService.findAll(pageable);

        return posts;
    }

    // 키워드로 게시글 조회
    @GetMapping("/search")
    public List<PostDto> searchPosts(@RequestParam("keyword") String keyword) {
        List<PostDto> posts = postService.searchByKeyword(keyword);

        return posts;
    }

    // 게시글ID로 상세 조회
    @GetMapping("/{festival-id}")
    public DetailPostDto getPost(@PathVariable("festival-id") String festivalId) {
        DetailPostDto detailPostDto = postService.searchByFestivalId(festivalId);

        return detailPostDto;
    }

    // 장르별 게시글 조회(축제, 연극, 무용(서양/한국무용), 대중무용, 서양음악(클래식),
    // 한국음악(국악), 대중음악, 복합, 서커스/마술, 뮤지컬)
    @GetMapping("/select")
    public List<PostDto> selectGenrePosts(@RequestParam("genre") String genre, @RequestParam(value = "count", required = false) Integer count) {
        List<PostDto> posts = postService.searchByGenrenm(genre, count);

        return posts;
    }

    // 지역 기준으로 게시글 조회
    @GetMapping("/view")
    public List<PostDto> areaPosts(@RequestParam("area") String area, @RequestParam(value = "count", required = false) Integer count) {
        List<PostDto> posts = postService.searchByFestivalArea(area, count);

        return posts;
    }
}
