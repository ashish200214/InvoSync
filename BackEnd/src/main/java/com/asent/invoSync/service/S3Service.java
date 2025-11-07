package com.asent.invoSync.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class S3Service {

    @Value("${aws.accessKeyId}") 
    private String accessKey;

    @Value("${aws.secretAccessKey}") 
    private String secretKey;

    @Value("${aws.s3.bucketName}") 
    private String bucketName;

    @Value("${aws.region}") 
    private String region;

    private S3Client client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    /**
     * Upload file to structured folder:
     * {customerWhatsApp}/{subFolder}/{baseName}.{ext}
     */
    public String uploadFileWithCustomerFolder(MultipartFile file, String customerWhatsApp, String subFolder, String baseName)
            throws IOException {

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }

        String key = customerWhatsApp + "/" + subFolder + "/" + baseName + ext;

        client().putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }
}
