//package com.radioheaven.radioheaven_backend.service;
//
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.core.async.AsyncResponseTransformer;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3AsyncClient;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;
//
//import java.nio.file.Paths;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class StorageService {
//
//    S3AsyncClient s3AsyncClient;
//
//    public StorageService() {
//        this.s3AsyncClient = S3AsyncClient.builder()
//                .region(Region.US_EAST_1) // Set your region
//                .credentialsProvider(DefaultCredentialsProvider.create())
//                .build();
//    }
//
//    public CompletableFuture<GetObjectResponse> downloadFile(String bucketName, String key, String downloadFilePath) {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        return s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toFile(Paths.get(downloadFilePath)));
//    }
//
//}
