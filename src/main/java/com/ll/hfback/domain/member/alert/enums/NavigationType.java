package com.ll.hfback.domain.member.alert.enums;

import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum NavigationType {
  GROUP(new String[]{"chatRoomId"}),  // 해당 모임으로 이동
  BOARD(new String[]{"boardId"}),  // 해당 공지사항으로 이동
  FESTIVAL(new String[]{"festivalId"}),  // 해당 축제로 이동
  COMMENT(new String[]{"commentId"}), // 해당 댓글로 이동
  NONE(new String[]{});  // 이동 없음 (확인 처리만 됨)

  private final String[] navigationDataKeys;

  public void validateNavigationData(Map<String, Object> navigationData) {
    for (String key : navigationDataKeys) {
      if (!navigationData.containsKey(key)) {
        throw new ServiceException(ErrorCode.NAVI_VALIDATE_ERROR);
      }
    }
  }

}
