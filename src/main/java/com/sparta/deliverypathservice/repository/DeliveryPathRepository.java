package com.sparta.deliverypathservice.repository;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {

    Page<DeliveryPath> findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull(UUID sHubId, UUID eHubId, Pageable pageable);

    Page<DeliveryPath> findAllByHubDeliveryUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
}
