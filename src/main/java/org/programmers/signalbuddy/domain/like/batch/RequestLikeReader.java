package org.programmers.signalbuddy.domain.like.batch;

import static org.programmers.signalbuddy.domain.like.service.LikeService.getLikeKeyPrefix;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.dto.LikeUpdateRequest;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestLikeReader implements ItemStreamReader<LikeUpdateRequest> {

    private final StringRedisTemplate redisTemplate;
    private Cursor<byte[]> cursor;

    @Override
    public void open(ExecutionContext executionContext) {
        ScanOptions scanOptions = ScanOptions.scanOptions()
            .match(getLikeKeyPrefix() + "*")
            .build();

        cursor = redisTemplate.executeWithStickyConnection(
            connection -> connection.scan(scanOptions));
    }

    @Override
    public LikeUpdateRequest read() throws Exception {
        if (cursor != null && cursor.hasNext()) {
            String key = new String(cursor.next(), StandardCharsets.UTF_8);
            String[] keyInfo = key.split(":");

            Long feedbackId = Long.parseLong(keyInfo[1]);
            Long memberId = Long.parseLong(keyInfo[2]);
            String likeRequestType = redisTemplate.opsForValue().get(key);

            return LikeUpdateRequest.builder()
                .feedbackId(feedbackId)
                .memberId(memberId)
                .likeRequestType(likeRequestType)
                .build();
        }

        return null;
    }
}
