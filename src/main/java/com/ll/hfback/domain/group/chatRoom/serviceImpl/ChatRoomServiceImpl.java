package com.ll.hfback.domain.group.chatRoom.serviceImpl;

import com.ll.hfback.domain.group.chatRoom.auth.AuService;
import com.ll.hfback.domain.group.chatRoom.dto.ChatRoomDto;
import com.ll.hfback.domain.group.chatRoom.dto.DetailChatRoomDto;
import com.ll.hfback.domain.group.chatRoom.entity.ChatRoom;
import com.ll.hfback.domain.group.chatRoom.form.CreateChatRoomForm;
import com.ll.hfback.domain.group.chatRoom.form.UpdateChatRoomForm;
import com.ll.hfback.domain.group.chatRoom.repository.ChatRoomRepository;
import com.ll.hfback.domain.group.chatRoom.service.ChatRoomService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final AuService auService;

    // 해당 게시글의 모든 모임 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> searchByFestivalId(String festivalId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByFestivalId(festivalId);
        return chatRooms.stream()
                .map(this::convertToChatRoomDto)  // convert ChatRoom to ChatRoomDto
                .collect(Collectors.toList());
    }

    // 해당 게시글의 모임 상세 조회
    @Override
    @Transactional(readOnly = true)
    public Optional<DetailChatRoomDto> searchById(Long id) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(id);

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        String memberId = String.valueOf(currentUserId);

        return chatRoom
                // joinMemberList에 memberId 포함 여부 확인
                .filter(room -> room.getJoinMemberIdList().contains(memberId))
                .map(this::convertToDetailChatRoomDto);
    }

    // DB에서 받아온 참여자명단을 List<String>으로 변환하고, ChatRoom을 ChatRoomDto로 변환하는 메서드
    private ChatRoomDto convertToChatRoomDto(ChatRoom chatRoom) {
        // Create ChatRoomDto
        ChatRoomDto chatRoomDto = new ChatRoomDto(
                chatRoom.getMember().getId(),
                chatRoom.getId(),
                chatRoom.getRoomTitle(),
                chatRoom.getRoomContent(),
                chatRoom.getRoomMemberLimit(),
                chatRoom.getJoinMemberIdList().size()
        );

        return chatRoomDto;
    }

    // DB에서 받아온 참여자명단과 대기자명단을 List<String>으로 변환하고, ChatRoom을 DetailChatRoomDto로 변환하는 메서드
    private DetailChatRoomDto convertToDetailChatRoomDto(ChatRoom chatRoom) {
        // 기존 참여자/대기자 ID 리스트를 가져옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();
        List<String> waitingMemberIdList = chatRoom.getWaitingMemberIdList();

        // ID를 사용하여 각 Member 엔티티를 조회하고 닉네임 리스트를 생성
        List<String> joinMemberNicknames = joinMemberIdList.stream()
                .map(id -> memberRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다.")).getNickname())
                .toList();
        List<String> waitingMemberNicknames = waitingMemberIdList.stream()
                .map(id -> memberRepository.findById(Long.valueOf(id))
                        .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다.")).getNickname())
                .toList();

        // DetailChatRoomDto 생성
        DetailChatRoomDto detailChatRoomDto = new DetailChatRoomDto(
                chatRoom.getMember().getId(),
                chatRoom.getId(),
                chatRoom.getMember().getNickname(),
                chatRoom.getRoomTitle(),
                chatRoom.getRoomContent(),
                chatRoom.getJoinMemberIdList(),
                joinMemberNicknames,  // 닉네임 리스트 전달
                chatRoom.getWaitingMemberIdList(),
                waitingMemberNicknames,
                chatRoom.getRoomMemberLimit(),
                joinMemberNicknames.size(),  // 참여자 수 전달
                waitingMemberNicknames.size()  // 대기자 수 전달
        );

        return detailChatRoomDto;
    }

    // 해당 게시글에 모임 생성
    @Override
    @Transactional
    public void createChatRoom(String festivalId, @Valid CreateChatRoomForm createChatRoomForm) {
        // roomMemberLimit의 값이 유효한지 확인하기 (10-100)
        Long roomMemberLimit = createChatRoomForm.getRoomMemberLimit();
        if (roomMemberLimit == null || roomMemberLimit < 10 || roomMemberLimit > 100) {
            throw new IllegalArgumentException("채팅방 참여 인원수는 10-100 사이의 값이어야 합니다.");
        }

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        String memberId = String.valueOf(currentUserId);

        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = auService.getCurrentMember();

        // 작성자의 이름을 참여자 명단에 추가 및 변환
        List<String> joinMemberIdList = new ArrayList<>();
        joinMemberIdList.add(memberId); // Member 객체의 ID를 추가

        // 비어있는 대기자 명단 생성 및 변환
        List<String> waitingMemberIdList = new ArrayList<>();

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .festivalId(festivalId)
                .roomTitle(createChatRoomForm.getRoomTitle())
                .roomContent(createChatRoomForm.getRoomContent())
                .roomMemberLimit(createChatRoomForm.getRoomMemberLimit())
                .roomState(0L)
                .joinMemberIdList(joinMemberIdList)
                .waitingMemberIdList(waitingMemberIdList)
                .build();

        chatRoomRepository.save(chatRoom);
    }

    // 해당 모임채팅방 수정
    @Override
    @Transactional
    public void updateChatRoom(Long chatRoomId, @Valid UpdateChatRoomForm updateChatRoomForm) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("모임 채팅방 수정 권한이 없습니다.");
        }

        chatRoom.setRoomTitle(updateChatRoomForm.getRoomTitle());
        chatRoom.setRoomContent(updateChatRoomForm.getRoomContent());
        chatRoom.setRoomMemberLimit(updateChatRoomForm.getRoomMemberLimit());
        chatRoomRepository.save(chatRoom);
    }

    // 해당 모임채팅방 삭제
    @Override
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("모임 채팅방 삭제 권한이 없습니다.");
        }

        chatRoomRepository.delete(chatRoom);
    }

    // 해당 모임채팅방에 참여신청
    @Override
    @Transactional
    public void applyChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        String memberId = String.valueOf(currentUserId);

        // 기존 참여자/대기자 명단을 변환하여 불러옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();
        List<String> waitingMemberIdList = chatRoom.getWaitingMemberIdList();

        // 참여자/대기자 명단 등록여부 확인 및 사용자 ID 추가
        if (joinMemberIdList.contains(memberId)) {
            throw new IllegalStateException("이미 참여자 명단에 등록된 사용자입니다.");
        } else {
            if (!waitingMemberIdList.contains(memberId)) {
                waitingMemberIdList.add(memberId);
            } else {
                throw new IllegalStateException("이미 대기자 명단에 등록된 사용자입니다.");
            }
        }

        // 변경된 대기자 명단을 다시 저장
        chatRoom.setWaitingMemberIdList(waitingMemberIdList);
        chatRoomRepository.save(chatRoom); // 트랜잭션 종료 시 자동 저장
    }

    // 해당 모임채팅방에 참여신청 취소
    @Override
    @Transactional
    public void cancelApplyChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        String memberId = String.valueOf(currentUserId);

        // 기존 대기자 명단을 변환하여 불러옴
        List<String> waitingMemberIdList = chatRoom.getWaitingMemberIdList();

        // 대기자 명단 등록여부 확인 및 사용자 ID 제거
        if (waitingMemberIdList.contains(memberId)) {
            waitingMemberIdList.remove(memberId);
        } else {
            throw new IllegalStateException("대기자 명단에 등록되지 않은 사용자입니다.");
        }

        // 변경된 대기자 명단을 다시 저장
        chatRoom.setWaitingMemberIdList(waitingMemberIdList);
        chatRoomRepository.save(chatRoom); // 트랜잭션 종료 시 자동 저장
    }

    // 해당 모임채팅방 참여신청 승인
    @Override
    @Transactional
    public void approveApplyChatRoom(Long chatRoomId, String applyMemberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("모임 참여신청 승인 권한이 없습니다.");
        }

        // 기존 참여자/대기자 명단을 변환하여 불러옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();
        List<String> waitingMemberIdList = chatRoom.getWaitingMemberIdList();

        // 참여자 수가 제한 인원수를 넘으면 안됨
        if (chatRoom.getRoomMemberLimit() == (long)joinMemberIdList.size()){
            throw new IllegalStateException("참여자 수가 제한 인원수에 도달하였습니다.");
        } else {
            // 참여자/대기자 명단 등록여부 확인 및 사용자 ID 추가/제거
            if (waitingMemberIdList.contains(applyMemberId)) {
                waitingMemberIdList.remove(applyMemberId);
                joinMemberIdList.add(applyMemberId);
            } else {
                throw new IllegalStateException("대기자 명단에 등록되지 않은 사용자입니다.");
            }
        }
    }

    // 해당 모임채팅방 참여신청 거절
    @Override
    @Transactional
    public void refuseApplyChatRoom(Long chatRoomId, String applyMemberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("모임 참여신청 거절 권한이 없습니다.");
        }

        // 기존 대기자 명단을 변환하여 불러옴
        List<String> waitingMemberIdList = chatRoom.getWaitingMemberIdList();

        // 대기자 명단 등록여부 확인 및 사용자 ID 제거
        if (waitingMemberIdList.contains(applyMemberId)) {
            waitingMemberIdList.remove(applyMemberId);
        } else {
            throw new IllegalStateException("대기자 명단에 등록되지 않은 사용자입니다.");
        }
    }

    // 해당 모임채팅방의 참여자 강퇴
    @Override
    @Transactional
    public void unqualifyChatRoom(Long chatRoomId, String memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("참여자 강퇴 권한이 없습니다.");
        }
        if (String.valueOf(currentUserId).equals(memberId)){
            throw new IllegalStateException("방장은 스스로 강퇴할 수 없습니다.");
        }

        // 기존 참여자 명단을 변환하여 불러옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();

        // 참여자 명단 등록여부 확인 및 사용자 ID 제거
        if (!joinMemberIdList.contains(memberId)) {
            throw new IllegalStateException("참여자 명단에 등록되지 않은 사용자입니다.");
        } else {
            joinMemberIdList.remove(memberId);
        }
    }

    // 해당 모임채팅방 나가기(방장이 나가는 경우 해당 모임채팅방 삭제)
    @Override
    @Transactional
    public void leaveChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        String memberId = String.valueOf(currentUserId);

        // 기존 참여자 명단을 변환하여 불러옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();

        // 방장이 나가면 모임채팅방 삭제
        if (currentUserId.equals(chatRoom.getMember().getId())) {
            deleteChatRoom(chatRoomId);
        } else {
            // 참여자 명단 등록여부 확인 및 사용자 ID 제거
            if (!joinMemberIdList.contains(memberId)) {
                throw new IllegalStateException("참여자 명단에 등록되지 않은 사용자입니다.");
            } else {
                joinMemberIdList.remove(memberId);
            }
        }
    }

    // 해당 모임채팅방에서 참여자에게 방장권한 위임
    @Override
    @Transactional
    public void delegateChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        String memberIdStr = String.valueOf(memberId);

        // 사용자 검증 - 현재 로그인한 사용자의 ID를 가져오는 메서드
        Long currentUserId = auService.getCurrentUserId();
        if (!chatRoom.getMember().getId().equals(currentUserId)) {
            throw new IllegalStateException("방장권한 위임 권한이 없습니다.");
        }

        // 기존 참여자 명단을 변환하여 불러옴
        List<String> joinMemberIdList = chatRoom.getJoinMemberIdList();

        // 참여자 명단 등록여부 확인 및 방장을 입력받은 사용자 ID로 변경
        if (!joinMemberIdList.contains(memberIdStr)) {
            throw new IllegalStateException("참여자 명단에 등록되지 않은 사용자입니다.");
        } else {
            chatRoom.setMember(member);
        }
    }
}
