package com.ll.hfback.global.exceptions;


import com.ll.hfback.global.rsData.RsData;
import com.ll.hfback.standard.base.Empty;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

  private final ErrorCode errorCode;

  public ServiceException(ErrorCode errorCode) {
    super(errorCode.getCode() + " : " + errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public RsData<Empty> getRsData() {
    return new RsData<>(errorCode.getCode(), errorCode.getMessage());
  }
}
