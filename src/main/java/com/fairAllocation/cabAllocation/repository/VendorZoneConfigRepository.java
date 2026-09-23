package com.fairAllocation.cabAllocation.repository;

import com.fairAllocation.cabAllocation.entity.VendorZoneConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Repository
public interface VendorZoneConfigRepository extends JpaRepository<VendorZoneConfig,Long>{

    @Cacheable(value="vendorConfigs" , key="#zoneType + '-' + #tripCategory")
    List<VendorZoneConfig> findByZoneTypeAndTripCategory(String zoneType, String tripCategory);

    @CacheEvict(value="vendorConfigs", allEntries = true)
    <S extends VendorZoneConfig> S save(S entity);
}