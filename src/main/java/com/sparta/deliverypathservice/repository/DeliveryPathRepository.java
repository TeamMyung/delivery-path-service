package com.sparta.deliverypathservice.repository;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {

}
