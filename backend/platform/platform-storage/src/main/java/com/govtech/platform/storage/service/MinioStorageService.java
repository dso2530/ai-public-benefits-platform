package com.govtech.platform.storage.service;

import com.govtech.platform.storage.config.StorageProperties;
import com.govtech.platform.storage.exception.StorageException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "minio", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

  private final MinioClient minioClient;
  private final StorageProperties properties;

  @PostConstruct
  public void init() throws Exception {

    if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.bucket()).build())) {

      minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.bucket()).build());
    }
  }

  @Override
  public String upload(InputStream inputStream, long size, String contentType, String bucket, String objectKey) {

    try {
      minioClient.putObject(
          PutObjectArgs.builder().bucket(bucket).object(objectKey).stream(
              inputStream, size, -1)
              .contentType(contentType)
              .build());

      return objectKey;

    } catch (Exception e) {
      throw new StorageException("Unable to upload object " + objectKey, e);
    }
  }

  @Override
  public InputStream download(String bucket, String objectKey) {

    try {
      return minioClient.getObject(
          GetObjectArgs.builder().bucket(bucket).object(objectKey).build());

    } catch (Exception e) {
      throw new StorageException("Unable to download object " + objectKey, e);
    }
  }

  @Override
  public void delete(String bucket, String objectKey) {

    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build());

    } catch (Exception e) {
      throw new StorageException("Unable to delete object " + objectKey, e);
    }
  }

  @Override
  public boolean exists(String bucket, String objectKey) {

    try {

      minioClient.statObject(
          StatObjectArgs.builder().bucket(bucket).object(objectKey).build());

      return true;

    } catch (ErrorResponseException e) {
      return false;
    } catch (Exception e) {
      throw new StorageException("Unable to check object " + objectKey, e);
    }
  }

  @Override
  public Iterable<Result<Item>> list(String bucket, String prefix) {

    return minioClient.listObjects(
        ListObjectsArgs.builder()
            .bucket(bucket)
            .prefix(prefix)
            .recursive(true)
            .build());
  }
}
