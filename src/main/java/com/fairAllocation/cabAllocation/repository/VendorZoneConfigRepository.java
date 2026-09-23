package com.fairAllocation.cabAllocation.repository;

import com.fairAllocation.cabAllocation.entity.VendorZoneConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorZoneConfigRepository extends JpaRepository<VendorZoneConfig,Long>{

    List<VendorZoneConfig> findByZoneTypeAndTripCategory(String zoneType, String tripCategory);
}