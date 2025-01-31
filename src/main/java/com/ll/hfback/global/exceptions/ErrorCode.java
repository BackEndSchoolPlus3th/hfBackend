package com.ll.hfback.global.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // Auth 관련 에러 (400번대)
  INVALID_AUTH_TOKEN(401, "AUTH_001", "유효하지 않은 토큰입니다."),
  TOKEN_EXPIRED(401, "AUTH_002", "만료된 토큰입니다."),
  SOCIAL_ONLY_ACCOUNT(401, "AUTH_003", "최초 소셜 로그인으로만 가입된 계정입니다. 비밀번호를 먼저 설정해주세요."),
  INVALID_PASSWORD(401, "AUTH_004", "비밀번호가 일치하지 않습니다."),
  INVALID_EMAIL(404, "AUTH_005", "해당 이메일로 가입된 정보가 없습니다."),
  UNAUTHORIZED(401, "AUTH_006", "인증된 사용자 정보가 없습니다."),
  REDIRECT_URL_NOT_FOUND(400, "AUTH_007", "리다이렉트 URL을 찾을 수 없습니다."),


  // Member 관련 에러
  MEMBER_NOT_FOUND(404, "MEMBER_001", "사용자를 찾을 수 없습니다."),
  DUPLICATE_EMAIL(409, "MEMBER_002", "이미 사용중인 이메일입니다."),
  INVALID_LOGIN_TYPE(400, "MEMBER_003", "지원하지 않는 로그인 타입입니다."),
  INVALID_ROLE(400, "MEMBER_004", "권한이 없습니다."),

  // Report 관련 에러
  REPORT_NOT_FOUND(404, "REPORT_001", "해당 신고를 찾을 수 없습니다."),
  REPORTER_NOT_FOUND(404, "REPORT_002", "신고자를 찾을 수 없습니다."),
  REPORTED_NOT_FOUND(404, "REPORT_003", "신고 대상자를 찾을 수 없습니다."),
  REPORTER_SELF_REPORT(400, "REPORT_004", "자기 자신을 신고할 수 없습니다."),

  // Storage 관련 에러
  FILE_NOT_FOUND(404, "STORAGE_001", "파일을 찾을 수 없습니다."),
  FILE_UPLOAD_ERROR(500, "STORAGE_002", "파일 업로드에 실패했습니다."),
  FILE_DELETE_ERROR(500, "STORAGE_003", "파일 삭제에 실패했습니다."),

  // Business 에러 (500번대)
  INTERNAL_ERROR(500, "SYS_001", "내부 시스템 에러");


  private final int status;
  private final String code;
  private final String message;


  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
