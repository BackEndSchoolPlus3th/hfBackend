package com.ll.hfback.domain.festival.comment.serviceImpl;

import com.ll.hfback.domain.festival.comment.dto.CommentDto;
import com.ll.hfback.domain.festival.comment.entity.Comment;
import com.ll.hfback.domain.festival.comment.form.AddCommentForm;
import com.ll.hfback.domain.festival.comment.form.UpdateCommentForm;
import com.ll.hfback.domain.festival.comment.repository.CommentRepository;
import com.ll.hfback.domain.festival.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    // 해당 게시글에 작성된 모든 댓글 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> searchByFestivalId(String festivalId) {
        List<Comment> comments = commentRepository.findByFestivalId(festivalId);
        return comments.stream()
                .map(this::convertToDto)
                .toList();
    }

    // 해당 댓글에 작성된 모든 답글 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> searchBySuperCommentId(Long superCommentId) {
        List<Comment> comments = commentRepository.findBySuperCommentId(superCommentId);
        return comments.stream()
                .map(this::convertToDto)
                .toList();
    }

    // 해당 게시글에 댓글 생성
    @Override
    @Transactional
    public void addComment(String festivalId, @Valid AddCommentForm addCommentForm) {
        Comment comment = Comment.builder()
                .festivalId(festivalId)
                .member(addCommentForm.getMember())
                .content(addCommentForm.getContent())
                .commentState(true)
                .superCommentId(addCommentForm.getSuperCommentId())
                .build();
        commentRepository.save(comment);
    }

    // 해당 댓글 수정
    @Override
    @Transactional
    public void updateComment(Long commentId, @Valid UpdateCommentForm updateCommentForm) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.setContent(updateCommentForm.getContent());
        commentRepository.save(comment);
    }

    // 해당 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

    // Comment -> CommentDto 변환
    @Override
    @Transactional
    public CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getMember().getId(),
                comment.getMember().getNickname(),
                comment.getId(),
                comment.getContent(),
                comment.getCreateDate(),
                comment.getSuperCommentId()
        );
    }
}
