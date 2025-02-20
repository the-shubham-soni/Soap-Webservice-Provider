package com.springbootsoap.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String S3_BUCKET_NAME;

    @Value("${cloud.aws.region.static}")
    private String REGION;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(byte[] fileContent, String fileName) {
        try {
            // Save to a temporary file
            Path tempFilePath = Files.createTempFile("Employee_", ".pdf");
            Files.write(tempFilePath, fileContent);

            // Upload the file to S3
            String s3FileName = "employees/" + fileName;
            s3Client.putObject(b -> b.bucket(S3_BUCKET_NAME).key(s3FileName).build(),
                    RequestBody.fromFile(tempFilePath));

            // Cleanup
            Files.deleteIfExists(tempFilePath);

            return "https://" + S3_BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + s3FileName;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading PDF to S3", e);
        }
    }
}
