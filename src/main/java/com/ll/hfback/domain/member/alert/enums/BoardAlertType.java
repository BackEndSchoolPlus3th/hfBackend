package com.ll.hfback.domain.member.alert.enums;

import com.ll.hfback.domain.member.alert.entity.Alert.AlertDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardAlertType implements AlertType {

  BOARD_NEW(
      "전체 공지\n새로운 공지사항이 등록되었습니다",
      NavigationType.BOARD
  ),
  SYSTEM_MAINTENANCE(
      "전체 공지\n시스템 점검 안내",
      NavigationType.BOARD
  );



  private final String messageTemplate;
  private final NavigationType navigationType;

  @Override
  public AlertDomain getDomain() {
    return AlertDomain.BOARD;
  }
}
