package com.radioheaven.radioheaven_backend.service;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B2SignedUrlService {
    public void generatePresignedUrl() {
        String endpoint = "https://s3.us-east-005.backblazeb2.com"; // Change region if needed
        String accessKey = "YOUR_B2_KEY_ID";
        String secretKey = "";
        String bucket = "radio-heaven";
        String key = "songs/NEER_ILLA.mp3"; // path in bucket

        // Change this to the endpoint from your bucket details, prefixed with "https://"
        String ENDPOINT_URL = "https://s3.us-east-005.backblazeb2.com";

        Matcher matcher = Pattern.compile("https://s3\\.([a-z0-9-]+)\\.backblazeb2\\.com").matcher(ENDPOINT_URL);
        if (!matcher.find()) {
            System.err.println("Can't find a region in the endpoint URL: " + ENDPOINT_URL);
        }
        String region = matcher.group(1);

        // Create a client. The try-with-resources pattern ensures the client is cleaned up when we're done with it
        try (S3Client b2 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create("b2tutorial"))
                .endpointOverride(new URI(ENDPOINT_URL)).build()) {

            // Get the list of buckets
            List<Bucket> buckets = b2.listBuckets().buckets();

            // Iterate through list, printing each bucket's name
            System.out.println("Buckets in account:");
            for (Bucket bucket1 : buckets) {
                System.out.println(bucket1.name());
            }

//        S3Presigner presigner = S3Presigner.builder()
//                .endpointOverride(java.net.URI.create(endpoint))
//                .region(Region.US_EAST_1) // match your bucket region
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create(accessKey, secretKey)))
//                .build();
//        AwsSessionCredentials awsCreds = AwsSessionCredentials.create(ACCESS_KEY, SECRET_ACCESS_KEY, "");
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(15)) // URL valid for 15 minutes
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        URL presignedUrl = presigner.presignGetObject(presignRequest).url();
//        System.out.println("Presigned URL: " + presignedUrl);
//
//        presigner.close();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}