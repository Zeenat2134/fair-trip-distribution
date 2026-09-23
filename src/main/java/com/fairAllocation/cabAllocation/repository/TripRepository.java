package com.fairAllocation.cabAllocation.repository;

import com.fairAllocation.cabAllocation.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TripRepository  extends JpaRepository<Trip,Long> {

}