package com.radioheaven.radioheaven_backend.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

public class B2SignedUrlService {
    public void generatePresignedUrl() {
        String endpoint = "https://s3.us-east-005.backblazeb2.com"; // Change region if needed
        String accessKey = "YOUR_B2_KEY_ID";
        String secretKey = "";
        String bucket = "your-bucket-name";
        String key = "songs/NEER_ILLA.mp3"; // path in bucket

        S3Presigner presigner = S3Presigner.builder()
                .endpointOverride(java.net.URI.create(endpoint))
                .region(Region.US_EAST_1) // match your bucket region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15)) // URL valid for 15 minutes
                .getObjectRequest(getObjectRequest)
                .build();

        URL presignedUrl = presigner.presignGetObject(presignRequest).url();
        System.out.println("Presigned URL: " + presignedUrl);

        presigner.close();
    }
}