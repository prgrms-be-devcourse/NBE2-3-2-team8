package org.programmers.signalbuddy.domain.crossroad.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import org.programmers.signalbuddy.domain.crossroad.entity.Crossroad;
import org.programmers.signalbuddy.domain.crossroad.repository.CrossroadRepository;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class CrossroadServiceTest extends ServiceTest {

    @Autowired
    private CrossroadService crossroadService;

    @Autowired
    private CrossroadRepository crossroadRepository;

    @MockitoBean
    private CrossroadProvider crossroadProvider;

    @DisplayName("Open API를 요청할 때, 페이지 수와 사이즈를 지정하고 다음 해당 데이터 저장")
    @Test
    void saveCrossroadDates() {
        // given
        List<CrossroadApiResponse> expectedList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expectedList.add(
                CrossroadApiResponse.builder()
                    .crossroadApiId("aaaaa" + i)
                    .name("aaaa" + i)
                    .lat(i * 5.0)
                    .lng(i * 7.0)
                    .build());
        }

        // when
        when(crossroadProvider.requestCrossroadApi(0, 10)).thenReturn(expectedList);
        crossroadService.saveCrossroadDates(0, 10);

        // then
        List<Crossroad> actual = crossroadRepository.findAll();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.size()).isEqualTo(expectedList.size());
            softAssertions.assertThat(actual.get(5).getCrossroadApiId()).isEqualTo(expectedList.get(5).getCrossroadApiId());
            softAssertions.assertThat(actual.get(5).getName()).isEqualTo(expectedList.get(5).getName());
            softAssertions.assertThat(actual.get(5).getCoordinate().getX()).isEqualTo(expectedList.get(5).getLng());
            softAssertions.assertThat(actual.get(5).getCoordinate().getY()).isEqualTo(expectedList.get(5).getLat());
        });
    }
}