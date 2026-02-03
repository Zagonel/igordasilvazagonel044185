package br.com.zagonel.catalogo_musical_api.service.minio;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RetriveLinkPreAssinadoMinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    /**
     * Gera uma URL temporária para download/visualização da capa do álbum.
     *
     * @param path O caminho do objeto armazenado no MinIO (extraído do CapaAlbumEmbeddable).
     * @return String contendo a URL assinada válida por 30 minutos.
     */
    public String execute(String path) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .expiry(30, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recuperar link do MinIO para o path: " + path, e);
        }
    }
}
