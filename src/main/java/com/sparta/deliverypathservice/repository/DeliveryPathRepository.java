package com.sparta.deliverypathservice.repository;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {

    Page<DeliveryPath> findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull(UUID sHubId, UUID eHubId, Pageable pageable);

    Page<DeliveryPath> findAllByHubDeliveryUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Optional<DeliveryPath> findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(UUID id1, UUID hubId1, UUID id2, UUID hubId2);

    Optional<DeliveryPath> findByDeliveryPathIdAndDeletedAtIsNull(UUID id);

    Optional<DeliveryPath> findTopByOrderByCreatedAtDesc();
}
