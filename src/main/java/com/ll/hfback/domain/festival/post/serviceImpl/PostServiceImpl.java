package com.ll.hfback.domain.festival.post.serviceImpl;

import com.ll.hfback.domain.festival.post.dto.DetailPostDto;
import com.ll.hfback.domain.festival.post.dto.PostDto;
import com.ll.hfback.domain.festival.post.entity.Post;
import com.ll.hfback.domain.festival.post.repository.PostRepository;
import com.ll.hfback.domain.festival.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    // 모든 게시글 조회
    @Override
    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    // 키워드로 게시글 조회
    @Override
    public List<PostDto> searchByKeyword(String keyword) {
        List<Post> posts = postRepository.findByFestivalNameContaining(keyword);
        return posts.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    // 게시글ID로 상세 조회
    @Override
    public DetailPostDto searchByFestivalId(String festivalId) {
        Post post = postRepository.findByFestivalId(festivalId);
        return convertToDetailPostDto(post);
    }

    // 공연 장르별 게시글 조회
    @Override
    public List<PostDto> searchByGenrenm(String genre) {
        List<Post> posts = postRepository.findByGenrenm(genre);
        return posts.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    // Post를 PostDto로 변환
    private PostDto convertToPostDto(Post post) {
        return new PostDto(
                post.getFestivalId(),
                post.getFestivalName(),
                post.getFestivalStartDate(),
                post.getFestivalEndDate(),
                post.getFestivalUrl()
        );
    }

    // Post를 DetailPostDto로 변환
    private DetailPostDto convertToDetailPostDto(Post post) {
        return new DetailPostDto(
                post.getFestivalId(),
                post.getFestivalName(),
                post.getFestivalStartDate(),
                post.getFestivalEndDate(),
                post.getFestivalArea(),
                post.getFestivalHallName(),
                post.getFestivalUrl()
        );
    }
}