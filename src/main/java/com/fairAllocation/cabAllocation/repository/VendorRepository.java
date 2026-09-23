package com.fairAllocation.cabAllocation.repository;

import com.fairAllocation.cabAllocation.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long>{

}