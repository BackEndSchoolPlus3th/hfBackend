package com.ll.hfback.global.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileStorageHandler {

  private final StorageService storageService;


  public FileUploadResult handleFileUpload(FileUploadRequest request) {
    try {
      // 기존 파일의 경로를 알고 default.png가 아니면서 삭제를 원한다면  =>  기존 파일 삭제
      if (request.isDeleteOldFile() && request.getOldFilePath() != null && !request.getOldFilePath()
          .equals("default.png")) {
        storageService.delete(request.getFolderPath() + request.getOldFilePath());
      }

      // 파일이 없고 기본 파일명이 있을 경우 => 기본 파일명으로 리턴
      if (request.getFile() == null || request.getFile().isEmpty()) {
        if (request.getDefaultFileName() != null) {
          return FileUploadResult.builder()
              .fileName(request.getDefaultFileName())
              .build();
        }
        return null;
      }

      // 새 파일 업로드
      String filename = UUID.randomUUID().toString();
      HashMap<String, Object> options = new HashMap<>();
      options.put(StorageService.CONTENT_TYPE, request.getFile().getContentType());

      storageService.upload(
          request.getFolderPath() + filename,
          request.getFile().getInputStream(),
          options
      );

      return FileUploadResult.builder()
          .fileName(filename)
          .fullPath(request.getFolderPath() + filename)
          .build();

    } catch (Exception e) {
      throw new RuntimeException("파일 업로드에 실패했습니다.", e);
    }
  }
}
