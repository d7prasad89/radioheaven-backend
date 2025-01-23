package com.radioheaven.radioheaven_backend.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Service
public class StorageService {

    S3AsyncClient s3AsyncClient;

}
