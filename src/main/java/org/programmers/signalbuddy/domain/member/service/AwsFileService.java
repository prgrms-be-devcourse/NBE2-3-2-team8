package org.programmers.signalbuddy.domain.member.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AwsFileService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.folder}")
    private String profileImageDir;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${default.profile.image.path}")
    private String defaultProfileImagePath;

    /**
     * S3에서 프로필 이미지를 가져옵니다.
     *
     * @param filename S3에 저장된 파일명
     * @return Resource 객체
     * @throws IllegalStateException 유효하지 않은 URL 또는 읽을 수 없는 파일인 경우
     */
    public Resource getProfileImage(String filename) {
        final String filePath = profileImageDir + filename;
        final URL url = generateFileUrl(filePath);
        final Resource resource = new UrlResource(url);

        if (!resource.exists() || !resource.isReadable()) {
            log.error("유효하지 않은 파일: {}", filePath);
            throw new IllegalStateException("유효하지 않은 URL 또는 읽을 수 없는 파일입니다.");
        }

        return resource;
    }

    /**
     * S3에 프로필 이미지를 저장합니다.
     *
     * @param profileImage 업로드할 프로필 이미지 파일
     * @return 저장된 파일 이름
     * @throws IllegalStateException 파일 업로드 중 오류가 발생한 경우
     */
    public String saveProfileImage(MultipartFile profileImage) {
        final String uniqueFilename = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();
        final String key = profileImageDir + uniqueFilename;

        try (InputStream inputStream = profileImage.getInputStream()) {
            uploadToS3(key, inputStream, profileImage.getContentType(), profileImage.getSize());
            return uniqueFilename;
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", profileImage.getOriginalFilename(), e);
            throw new IllegalStateException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * S3 URL 생성
     *
     * @param key S3 객체 키
     * @return URL 객체
     */
    private URL generateFileUrl(String key) {
        final GetUrlRequest request = GetUrlRequest.builder().bucket(bucket).key(key).build();
        return s3Client.utilities().getUrl(request);
    }

    /**
     * S3에 파일 업로드
     *
     * @param key         S3 객체 키
     * @param inputStream 업로드할 파일의 InputStream
     * @param contentType 파일의 Content-Type
     * @param size        파일 크기
     * @throws IllegalStateException S3에 파일 업로드 중 오류가 발생한 경우
     */
    private void uploadToS3(String key, InputStream inputStream, String contentType, long size) {
        try {
            final PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket)
                .key(key).contentType(contentType).build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", key, e);
            throw new IllegalStateException("S3에 파일 업로드 중 오류가 발생했습니다. 객체 키: " + key, e);
        }
    }
}
