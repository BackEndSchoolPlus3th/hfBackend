package com.ll.hfback.global.storage;

import java.io.InputStream;
import java.util.Map;

public interface StorageService {

  String CONTENT_TYPE = "contentType";

  void upload(String filePath, InputStream in, Map<String, Object> options) throws Exception;

  void delete(String filePath) throws Exception;

}
