package com.ll.hfback.domain.group.chatRoom.service;

import com.ll.hfback.domain.group.chatRoom.dto.ChatRoomDto;
import com.ll.hfback.domain.group.chatRoom.dto.DetailChatRoomDto;
import com.ll.hfback.domain.group.chatRoom.form.CreateChatRoomForm;
import com.ll.hfback.domain.group.chatRoom.form.UpdateChatRoomForm;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {

    // 해당 게시글의 모든 모임 조회
    List<ChatRoomDto> searchByFestivalId(String festivalId);

    // 해당 게시글의 모임 상세 조회
    Optional<DetailChatRoomDto> searchById(Long id);

    // 해당 게시글에 모임 생성
    void createChatRoom(String festivalId, @Valid CreateChatRoomForm createChatRoomForm);

    // 해당 모임채팅방 수정
    void updateChatRoom(Long chatRoomId, @Valid UpdateChatRoomForm updateChatRoomForm);

    // 해당 모임채팅방 삭제
    void deleteChatRoom(Long chatRoomId);

    // 해당 모임채팅방에 참여신청
    void applyChatRoom(Long chatRoomId);

    // 해당 모임채팅방에 참여신청 취소
    void cancelApplyChatRoom(Long chatRoomId);

    // 해당 모임채팅방 참여신청 승인
    void approveApplyChatRoom(Long chatRoomId, String applyMemberId);

    // 해당 모임채팅방 참여신청 거절
    void refuseApplyChatRoom(Long chatRoomId, String applyMemberId);

    // 해당 모임채팅방의 참여자 강퇴
    void unqualifyChatRoom(Long chatRoomId, String memberId);

    // 해당 모임채팅방 나가기(방장이 나가는 경우 해당 모임채팅방 삭제)
    void leaveChatRoom(Long chatRoomId);

    // 해당 모임채팅방에서 참여자에게 방장권한 위임
    void delegateChatRoom(Long chatRoomId, Long memberId);
}
