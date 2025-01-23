package org.programmers.signalbuddy.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class AwsFileServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private AwsFileService awsFileService;

    @Test
    @DisplayName("프로필 이미지 저장 성공 테스트")
    void saveProfileImage_Success() throws IOException {
        String fileName = "test-image.png";
        MultipartFile mockFile = mock(MultipartFile.class);
        InputStream mockInputStream = new ByteArrayInputStream("dummy content".getBytes());

        when(mockFile.getOriginalFilename()).thenReturn(fileName);
        when(mockFile.getInputStream()).thenReturn(mockInputStream);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getSize()).thenReturn((long) "dummy content".length());

        // Mock S3Client의 동작 설정
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenReturn(null); // putObject의 반환값을 지정

        String result = awsFileService.saveProfileImage(mockFile);

        assertNotNull(result);
        assertTrue(result.contains(fileName));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("프로필 이미지 불러오기 성공 테스트")
    void getProfileImage_Success() throws MalformedURLException {
        String fileName = "test-image.png";
        String key = "profiles/" + fileName;
        String bucketName = "test-bucket";

        // Mock S3Utilities
        S3Utilities mockUtilities = mock(S3Utilities.class);
        URL mockUrl = new URL("https://example.com/" + key);
        when(mockUtilities.getUrl(any(GetUrlRequest.class))).thenReturn(mockUrl);

        // Mock S3Client
        when(s3Client.utilities()).thenReturn(mockUtilities);

        // bucket 과 profileImageDir에 값 넣기
        ReflectionTestUtils.setField(awsFileService, "bucket", bucketName);
        ReflectionTestUtils.setField(awsFileService, "profileImageDir", "profiles/");

        // Spy AwsFileService, mock UrlResource 생성
        AwsFileService spyService = spy(awsFileService);
        UrlResource mockResource = mock(UrlResource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(true);
        doReturn(mockResource).when(spyService).createUrlResource(mockUrl);

        Resource resource = spyService.getProfileImage(fileName);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    @DisplayName("프로필 이미지 저장 실패 테스트")
    void saveProfileImage_Fail() throws IOException {
        // Given
        String fileName = "test-image.png";
        MultipartFile mockFile = mock(MultipartFile.class);
        InputStream mockInputStream = new ByteArrayInputStream("dummy content".getBytes());

        when(mockFile.getOriginalFilename()).thenReturn(fileName);
        when(mockFile.getInputStream()).thenReturn(mockInputStream);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getSize()).thenReturn((long) "dummy content".length());

        // S3Client가 예외를 던지도록 설정
        doThrow(new RuntimeException("S3 upload failed"))
            .when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            awsFileService.saveProfileImage(mockFile);
        });

        assertTrue(exception.getMessage().contains(MemberErrorCode.S3_UPLOAD_FAILURE.getMessage()));

    }
}