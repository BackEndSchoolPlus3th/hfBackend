package com.ll.hfback.domain.festival.comment.service;

import com.ll.hfback.domain.festival.comment.dto.CommentDto;
import com.ll.hfback.domain.festival.comment.form.AddCommentForm;
import com.ll.hfback.domain.festival.comment.form.UpdateCommentForm;
import jakarta.validation.Valid;
import java.util.List;

public interface CommentService {
    // 해당 게시글에 작성된 모든 댓글 조회
    List<CommentDto> searchByFestivalId(String festivalId);

    // 해당 댓글에 작성된 모든 답글 조회
    List<CommentDto> searchBySuperCommentId(Long superCommentId);

    // 해당 게시글에 댓글 생성
    void addComment(String festivalId, @Valid AddCommentForm addCommentForm);

    // 해당 댓글 수정
    void updateComment(Long commentId, @Valid UpdateCommentForm updateCommentForm);

    // 해당 댓글 삭제
    void deleteComment(Long commentId);
}
