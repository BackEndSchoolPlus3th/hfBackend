package com.ll.hfback.global.storage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadResult {
  private String fileName;
  private String fullPath;
}
