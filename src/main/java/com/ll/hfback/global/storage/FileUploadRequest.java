package com.ll.hfback.global.storage;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class FileUploadRequest {
  private String folderPath;
  private MultipartFile file;
  private String oldFilePath;
  private boolean deleteOldFile;
  private String defaultFileName;
}
