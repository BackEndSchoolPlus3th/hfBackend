package com.ll.hfback.global.initData;

import com.ll.hfback.domain.festival.comment.service.CommentService;
import com.ll.hfback.domain.group.chat.request.RequestMessage;
import com.ll.hfback.domain.group.chat.service.ChatMessageService;
import com.ll.hfback.domain.group.chatRoom.service.ChatRoomService;
import com.ll.hfback.domain.member.auth.dto.SignupRequest;
import com.ll.hfback.domain.member.auth.service.AuthService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.Gender;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@Profile("!prod")
public class NotProd {
    @Bean
    public ApplicationRunner applicationRunner(
            AuthService authService,
            CommentService commentService,
            ChatRoomService chatroomservice,
            ChatMessageService chatMessageService
    ) {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Member member1 = authService.signup(
                    new SignupRequest(
                        "test1@test.com", "1234", "강남",
                        Gender.M, true, "010-1111-1111"
                    )
                );
                Member member2 = authService.signup(
                    new SignupRequest(
                        "test2@test.com", "1234", "남영동",
                        Gender.M, false, "010-2222-2222"
                    )
                );
                Member member3 = authService.signup(
                    new SignupRequest(
                        "test3@test.com", "1234", "역삼",
                        Gender.W, true, "010-3333-3333"
                    )
                );



                // 테스트 댓글&답글 생성
                //
                //
                // member1이 PF256158 공연게시글에 작성한 테스트 댓글
                // member1 로그인 로직 필요
//                AddCommentForm addCommentForm1 = new AddCommentForm();
//                addCommentForm1.setContent("이것은 member1이 작성한 테스트 댓글입니다.");
//                addCommentForm1.setSuperCommentId(null);
//                commentService.addComment("PF256158", addCommentForm1);

                // member2가 PF256158 공연게시글에 작성한 테스트 댓글
                // member2 로그인 로직 필요
//                AddCommentForm addCommentForm2 = new AddCommentForm();
//                addCommentForm2.setContent("이것은 member2가 작성한 테스트 댓글입니다.");
//                addCommentForm2.setSuperCommentId(null);
//                commentService.addComment("PF256158", addCommentForm2);

                // member3가 PF256158 공연게시글에서 memeber1의 댓글에 작성한 테스트 답글
                // member3 로그인 로직 필요
//                AddCommentForm addCommentForm3 = new AddCommentForm();
//                addCommentForm3.setContent("이것은 member3이 작성한 테스트 답글입니다.");
//                addCommentForm3.setSuperCommentId(1L);
//                commentService.addComment("PF256158", addCommentForm3);

                // member3가 PF256158 공연게시글에서 memeber2의 댓글에 작성한 테스트 답글
                // member3 로그인 로직 필요
//                AddCommentForm addCommentForm4 = new AddCommentForm();
//                addCommentForm4.setContent("이것은 member3이 작성한 테스트 답글입니다.");
//                addCommentForm4.setSuperCommentId(2L);
//                commentService.addComment("PF256158", addCommentForm4);



                // 테스트 댓글 수정
                //
                //
                // member1이 수정한 테스트 댓글(comment-id=1)
                // member1 로그인 로직 필요
//                UpdateCommentForm updateCommentForm = new UpdateCommentForm();
//                updateCommentForm.setContent("이것은 member1이 다시 수정한 테스트 댓글입니다.");
//                commentService.updateComment(1L, updateCommentForm);
                


                // 테스트 모임채팅방 생성
                //
                //
                // member1이 PF256158 공연게시글에서 만든 모임채팅방
                // member1 로그인 로직 필요
//                CreateChatRoomForm createChatRoomForm1 = new CreateChatRoomForm();
//                createChatRoomForm1.setRoomTitle("이것은 member1이 작성한 테스트 제목입니다.");
//                createChatRoomForm1.setRoomContent("이것은 member1이 작성한 테스트 본문입니다.");
//                createChatRoomForm1.setRoomMemberLimit(10L);
//                chatroomservice.createChatRoom("PF256158", createChatRoomForm1);

                // member2가 PF256158 공연게시글에서 만든 모임채팅방
                // member2 로그인 로직 필요
//                CreateChatRoomForm createChatRoomForm2 = new CreateChatRoomForm();
//                createChatRoomForm2.setRoomTitle("이것은 member2가 작성한 테스트 제목입니다.");
//                createChatRoomForm2.setRoomContent("이것은 member2가 작성한 테스트 본문입니다.");
//                createChatRoomForm2.setRoomMemberLimit(20L);
//                chatroomservice.createChatRoom("PF256158", createChatRoomForm2);

                // member3가 PF256158 공연게시글에서 만든 모임채팅방
                // member3 로그인 로직 필요
//                CreateChatRoomForm createChatRoomForm3 = new CreateChatRoomForm();
//                createChatRoomForm3.setRoomTitle("이것은 member3이 작성한 테스트 제목입니다.");
//                createChatRoomForm3.setRoomContent("이것은 member3이 작성한 테스트 본문입니다.");
//                createChatRoomForm3.setRoomMemberLimit(30L);
//                chatroomservice.createChatRoom("PF256158", createChatRoomForm3);



                // 테스트 모임채팅방 수정
                //
                //
                // member1이 수정한 테스트 모임채팅방(chat-room-id=1)
                // member1 로그인 로직 필요
//                UpdateChatRoomForm updateChatRoomForm = new UpdateChatRoomForm();
//                updateChatRoomForm.setRoomTitle("이것은 member1이 다시 수정한 테스트 제목입니다.");
//                updateChatRoomForm.setRoomContent("이것은 member1이 다시 수정한 테스트 본문입니다.");
//                updateChatRoomForm.setRoomMemberLimit(100L);
//                chatroomservice.updateChatRoom(1L, updateChatRoomForm);



                // 테스트 모임채팅방 참여신청
                //
                //
                // member1이 chat-room-id=2,3에 참여신청
                // member1 로그인 로직 필요
//                chatroomservice.applyChatRoom(2L);
//                chatroomservice.applyChatRoom(3L);

                // member2가 chat-room-id=1,3에 참여신청
                // member2 로그인 로직 필요
//                chatroomservice.applyChatRoom(1L);
//                chatroomservice.applyChatRoom(3L);

                // member3이 chat-room-id=1,2에 참여신청
                // member3 로그인 로직 필요
//                chatroomservice.applyChatRoom(1L);
//                chatroomservice.applyChatRoom(2L);



                // 1번 채팅방 채팅 테스트 (유저 셋이서 하는 대화)
                //
                //
                // 3명의 유저가 대화하는 채팅 메시지 추가 (30개)
                List<String> messages = List.of(
                        "안녕하세요! 오늘 공연 잘 보셨나요?",
                        "네, 정말 멋졌어요!",
                        "저도 너무 감동받았어요. 특히 마지막 장면에서요.",
                        "맞아요, 그 장면에서 눈물이 날 뻔했어요.",
                        "혹시 다음 공연도 같이 보실래요?",
                        "좋아요! 다음 공연은 언제인가요?",
                        "다음 주 토요일로 알고 있어요.",
                        "그럼 그때 같이 보기로 해요!",
                        "공연 후에 다 같이 저녁도 먹으면 좋겠네요.",
                        "좋은 생각이에요! 어디서 먹을까요?",
                        "강남역 근처에 괜찮은 식당이 많아요.",
                        "거기 좋네요. 강남역에서 만나기로 해요.",
                        "몇 시에 만날까요?",
                        "공연이 7시에 끝나니까 7시 반에 만나는 건 어때요?",
                        "좋아요. 그럼 7시 반에 강남역에서 봬요!",
                        "혹시 공연 티켓은 다들 예약하셨나요?",
                        "아직이요. 지금 예약하려고요.",
                        "저도 지금 바로 예약할게요.",
                        "저는 이미 예약했어요. 2구역에 자리 잡았어요.",
                        "그럼 저희도 2구역으로 예약해야겠네요.",
                        "같은 구역이면 더 재밌을 거예요!",
                        "저도 그렇게 생각해요. 공연이 기대돼요!",
                        "혹시 다음 주 공연이 몇 시에 시작하나요?",
                        "오후 5시 시작이에요. 공연장에 4시쯤 도착하면 될 것 같아요.",
                        "그럼 4시쯤 공연장에서 만나기로 해요.",
                        "좋아요. 공연 전에 간단히 커피 마시면서 얘기해요.",
                        "저 커피 좋아해요! 기대돼요.",
                        "그럼 공연장에서 뵐게요! 다들 좋은 하루 되세요.",
                        "네, 좋은 하루 되세요! 다음 주에 만나요.",
                        "감사합니다. 모두 다음 주에 뵈어요!"
                );
                //
                //
                for (int i = 0; i < messages.size(); i++) {
                    Member sender = switch (i % 3) {  // switch 표현식 사용
                        case 0 -> member1;
                        case 1 -> member2;
                        default -> member3;
                    };

                    RequestMessage requestMessage = new RequestMessage(sender.getId(), messages.get(i));

                    // 메시지 저장 호출
                    chatMessageService.writeMessage(1L, requestMessage
                    );
                }

            }
        };
    }
}
