package com.sparta.deliverypathservice.repository;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.global.config.AuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Import(AuditingConfig.class)
class DeliveryPathRepositoryTest {

//    @MockitoBean
//    TestEntityManager testEntityManager;

    @Autowired
    private DeliveryPathRepository deliveryPathRepository;

    private final UUID deliveryId = UUID.randomUUID();
    private final UUID sHubId = UUID.randomUUID();
    private final UUID eHubId = UUID.randomUUID();
    private final Long hubDeliveryUserId = 1L;
    private UUID deliveryPathId1;
    private UUID deliveryPathId2;
    private UUID deliveryPathId3;
    private UUID deliveryPathId4;
    private UUID deliveryPathId5;

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
        DeliveryPath deliveryPath5 = DeliveryPath.builder()
                .deliveryId(UUID.randomUUID())
                .sequence(2)
                .startHubId(UUID.randomUUID())
                .endHubId(UUID.randomUUID())
                .actual_distance(100)
                .actual_time(6000)
                .status(DeliveryPathState.HUB_MOVING)
                .hubDeliveryUserId(3L)
                .deletedAt(LocalDateTime.now())
                .build();

        deliveryPathId1 = deliveryPathRepository.save(deliveryPath1).getDeliveryPathId();  //배송id, shub, ehub, 배송자1, 허브대기중
        deliveryPathId2 = deliveryPathRepository.save(deliveryPath2).getDeliveryPathId();  //배송id, ehub, 배송자2, 배송완료
        deliveryPathId3 = deliveryPathRepository.save(deliveryPath3).getDeliveryPathId();  //배송자, 배송자1, 허브이동중
        deliveryPathId4 = deliveryPathRepository.save(deliveryPath4).getDeliveryPathId();  //배송자3, 허브이동중
        deliveryPathId5 = deliveryPathRepository.save(deliveryPath5).getDeliveryPathId();  //삭제
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


    @Test
    @DisplayName("허브관리자의 배송 경로 상세 조회")
    void findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId() {

        Optional<DeliveryPath> path1 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(deliveryPathId1, sHubId, deliveryPathId1, sHubId);
        Optional<DeliveryPath> path2 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(deliveryPathId2, sHubId, deliveryPathId2, sHubId);
        Optional<DeliveryPath> path3 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(deliveryPathId3, sHubId, deliveryPathId3, sHubId);
        Optional<DeliveryPath> path4 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(deliveryPathId4, sHubId, deliveryPathId4, sHubId);
        Optional<DeliveryPath> path5 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(deliveryPathId5, sHubId, deliveryPathId5, sHubId);

        assertTrue(path1.isPresent());
        assertTrue(path2.isPresent());
        assertFalse(path3.isPresent());
        assertFalse(path4.isPresent());
        assertFalse(path5.isPresent());
    }

    @Test
    @DisplayName("업체담당자의 배송 경로 상세 조회")
    void findByDeliveryPathIdAndDeletedAtIsNull() {

        Optional<DeliveryPath> path1 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(deliveryPathId1);
        Optional<DeliveryPath> path2 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(deliveryPathId2);
        Optional<DeliveryPath> path3 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(deliveryPathId3);
        Optional<DeliveryPath> path4 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(deliveryPathId4);
        Optional<DeliveryPath> path5 = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(deliveryPathId5);

        assertTrue(path1.isPresent());
        assertTrue(path2.isPresent());
        assertTrue(path3.isPresent());
        assertTrue(path4.isPresent());
        assertFalse(path5.isPresent());
    }

    @Test
    @DisplayName("가장 최근 배송 경로 상세 조회")
    void findTopByOrderByCreatedAtDesc() {

        Optional<DeliveryPath> path = deliveryPathRepository.findTopByOrderByCreatedAtDesc();

        System.out.println(path.get().getDeliveryPathId());
        System.out.println(deliveryPathId1);
        System.out.println(deliveryPathId2);
        System.out.println(deliveryPathId3);
        System.out.println(deliveryPathId4);
        System.out.println(deliveryPathId5);

        assertTrue(path.get().getDeliveryPathId() == deliveryPathId5);
    }
}