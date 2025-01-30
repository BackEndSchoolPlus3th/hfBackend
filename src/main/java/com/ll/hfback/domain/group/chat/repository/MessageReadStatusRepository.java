package com.ll.hfback.domain.group.chat.repository;

import com.ll.hfback.domain.group.chat.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.ll.hfback.domain.group.chat.repository
 * fileName       : MessageReadStatusRepository
 * author         : sungjun
 * date           : 2025-01-27
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-27        kyd54       최초 생성
 */
public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {
    Optional<MessageReadStatus> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
