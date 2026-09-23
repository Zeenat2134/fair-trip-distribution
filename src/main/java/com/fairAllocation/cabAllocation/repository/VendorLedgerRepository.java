package com.fairAllocation.cabAllocation.repository;

import com.fairAllocation.cabAllocation.entity.VendorLedger;
import com.fairAllocation.cabAllocation.entity.VendorZoneConfig;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorLedgerRepository  extends JpaRepository<VendorLedger,Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vl FROM VendorLedger vl WHERE vl.zoneType = :zoneType AND vl.tripCategory = :tripCategory")
    List<VendorLedger> findLedgersForAllocationWithLock(
            @Param("zoneType")String zoneType,
            @Param("tripCategory")String tripCategory
    );

    VendorLedger findByVendorIdAndZoneTypeAndTripCategory(Long vendorId, String zoneType, String tripCategory);
}