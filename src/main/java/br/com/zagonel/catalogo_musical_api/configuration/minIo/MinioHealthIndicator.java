package br.com.zagonel.catalogo_musical_api.configuration.minIo;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("minio")
@RequiredArgsConstructor
public class MinioHealthIndicator implements HealthIndicator {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public Health health() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (bucketExists) {
                return Health.up()
                        .withDetail("bucket", bucketName)
                        .withDetail("status", "Bucket Accessible")
                        .build();
            } else {
                return Health.down()
                        .withDetail("bucket", bucketName)
                        .withDetail("error", "Bucket not found")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("bucket", bucketName)
                    .withException(e)
                    .build();
        }
    }
}
