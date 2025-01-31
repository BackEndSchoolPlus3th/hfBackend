package com.ll.hfback.global.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;


@Slf4j
@Service
public class NcpObjectStorageService implements StorageService {

  private AmazonS3 s3;

  @Value("${ncp.storage.bucketname}")
  private String bucketName;


  public NcpObjectStorageService(
      @Value("${ncp.storage.endpoint}") String endPoint,
      @Value("${ncp.storage.regionname}") String regionName,
      @Value("${ncp.accesskey}") String accessKey,
      @Value("${ncp.secretkey}") String secretKey) {

    this.s3 = AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .build();
  }


  @Override
  public void upload(String filePath, InputStream in, Map<String, Object> options) throws Exception {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType((String) options.get(CONTENT_TYPE));

    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucketName,
        filePath, // 업로드 파일의 경로(폴더 경로 포함)
        in,
        objectMetadata
    ).withCannedAcl(CannedAccessControlList.PublicRead);

    s3.putObject(putObjectRequest);
  }


  @Override
  public void delete(String filePath) throws Exception {
    try {
      boolean exists = s3.doesObjectExist(bucketName, filePath);

      if (exists) {
        s3.deleteObject(bucketName, filePath);

      } else {
        throw new ServiceException(ErrorCode.FILE_NOT_FOUND);
      }
    } catch (AmazonS3Exception e) {
      throw new ServiceException(ErrorCode.FILE_DELETE_ERROR);
    }
  }
}
