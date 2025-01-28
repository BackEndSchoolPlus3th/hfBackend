package com.ll.hfback.domain.group.chat.service;

import com.ll.hfback.domain.group.chat.request.RequestMessage;
import com.ll.hfback.domain.group.chat.response.MessageReadStatusResponse;
import com.ll.hfback.domain.group.chat.response.MessageSearchKeywordsResponse;
import com.ll.hfback.domain.group.chat.response.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * packageName    : com.ll.hfback.domain.group.chat.service
 * fileName       : ChatMessageService
 * author         : sungjun
 * date           : 2025-01-22
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-22        kyd54       최초 생성
 */
public interface ChatMessageService {
    // 해당 채팅방에 채팅 메시지 저장
    void writeMessage(Long chatRoomId, ResponseMessage responseMessage);

    // 해당 채팅방의 모든 메시지 불러오기
    Page<RequestMessage> readMessages(Long chatRoomId, int page);

    // 메시지 불러올 때 사용할 커스텀 페이징
    Pageable customPaging(int page);

    // 조건에 따른 채팅 메시지 검색 기능
    Page<RequestMessage> searchMessages(Long chatRoomId,
                                        int page,
                                        MessageSearchKeywordsResponse messageSearchKeywordsResponse);
    
    // 메시지 읽음/안읽음 상태 확인
    void messageReadStatus(Long chatRoomId, MessageReadStatusResponse messageReadStatusResponse);
}
