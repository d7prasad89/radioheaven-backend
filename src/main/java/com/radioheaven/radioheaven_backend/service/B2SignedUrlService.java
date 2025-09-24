package com.radioheaven.radioheaven_backend.service;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B2SignedUrlService {
    public void generatePresignedUrl() {

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


            System.out.println("Your Amazon B2 buckets are:" + buckets.get(0));
            // Assume you have bucketName and key (object path)
            S3Presigner presigner = S3Presigner.builder().region(Region.of(region)).credentialsProvider(ProfileCredentialsProvider.create("b2tutorial")).build();
            for (Bucket bucket1 : buckets) {
                String bucketName = bucket1.name();
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

                String signedUrl = presigner.presignGetObject(presignRequest).url().toString();

                System.out.println("Signed URL: >>>> " + signedUrl);
                presigner.close();
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}