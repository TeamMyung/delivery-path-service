package com.sparta.deliverypathservice.repository;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class DeliveryPathRepositoryTest {

//    @MockitoBean
//    TestEntityManager testEntityManager;

    @Autowired
    private DeliveryPathRepository deliveryPathRepository;

    private final UUID deliveryPathId = UUID.randomUUID();
    private final UUID deliveryId = UUID.randomUUID();
    private final UUID sHubId = UUID.randomUUID();
    private final UUID eHubId = UUID.randomUUID();
    private final Long hubDeliveryUserId = 1L;

    @BeforeEach
    void setUp() {
        DeliveryPath deliveryPath1 = DeliveryPath.builder()
                .deliveryId(deliveryId)
                .sequence(1)
                .startHubId(sHubId)
                .endHubId(eHubId)
                .actual_distance(100)
                .actual_time(6000)
                .status(DeliveryPathState.HUB_WAIT)
                .hubDeliveryUserId(hubDeliveryUserId)
                .build();

        DeliveryPath deliveryPath2 = DeliveryPath.builder()
                .deliveryId(deliveryId)
                .sequence(2)
                .startHubId(UUID.randomUUID())
                .endHubId(sHubId)
                .actual_distance(100)
                .actual_time(6000)
                .status(DeliveryPathState.HUB_COMPLETE)
                .hubDeliveryUserId(2L)
                .build();
        DeliveryPath deliveryPath3 = DeliveryPath.builder()
                .deliveryId(UUID.randomUUID())
                .sequence(2)
                .startHubId(UUID.randomUUID())
                .endHubId(UUID.randomUUID())
                .actual_distance(100)
                .actual_time(6000)
                .status(DeliveryPathState.HUB_MOVING)
                .hubDeliveryUserId(hubDeliveryUserId)
                .build();
        DeliveryPath deliveryPath4 = DeliveryPath.builder()
                .deliveryId(UUID.randomUUID())
                .sequence(2)
                .startHubId(UUID.randomUUID())
                .endHubId(UUID.randomUUID())
                .actual_distance(100)
                .actual_time(6000)
                .status(DeliveryPathState.HUB_MOVING)
                .hubDeliveryUserId(3L)
                .build();

        deliveryPathRepository.save(deliveryPath1);
        deliveryPathRepository.save(deliveryPath2);
        deliveryPathRepository.save(deliveryPath3);
        deliveryPathRepository.save(deliveryPath4);
    }

    @Test
    @DisplayName("허브 관리자의 배송 경로 조회")
    void findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull() {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(1, 10, sort);

        Page<DeliveryPath> list = deliveryPathRepository.findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull(sHubId, sHubId, pageable);

        for (DeliveryPath deliveryPath : list) {
            assertTrue(deliveryPath.getStartHubId() == sHubId || deliveryPath.getEndHubId() == sHubId);
            assertNull(deliveryPath.getDeletedAt());
        }
    }

    @Test
    @DisplayName("허브 배송담당자의 배송 경로 조회")
    void findAllByHubDeliveryUserIdAndDeletedAtIsNull() {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(1, 10, sort);

        Page<DeliveryPath> list = deliveryPathRepository.findAllByHubDeliveryUserIdAndDeletedAtIsNull(hubDeliveryUserId, pageable);

        for (DeliveryPath deliveryPath : list) {
            assertEquals(hubDeliveryUserId, deliveryPath.getHubDeliveryUserId());
            assertNull(deliveryPath.getDeletedAt());
        }
    }
}