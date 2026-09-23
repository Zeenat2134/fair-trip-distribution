package com.fairAllocation.cabAllocation.controller;

import com.fairAllocation.cabAllocation.entity.Trip;
import com.fairAllocation.cabAllocation.service.AllocationService;
import  lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/allocation")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping("/allocate/{tripId}")
    public ResponseEntity<?> allocateTrip(@PathVariable Long tripId) {

            Trip allocatedTrip = allocationService.allocateTrip(tripId);
            return ResponseEntity.ok(allocatedTrip);
    }

    @PostMapping("/reject/{tripId}")
    public ResponseEntity<?> rejectTrip(@PathVariable Long tripId) {
        Trip reallocatedTrip= allocationService.rejectAndReallocate(tripId);
        return ResponseEntity.ok(reallocatedTrip);
    }
}
