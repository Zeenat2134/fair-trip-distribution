package com.fairAllocation.cabAllocation.service;

import com.fairAllocation.cabAllocation.entity.Trip;
import  com.fairAllocation.cabAllocation.entity.Vendor;
import com.fairAllocation.cabAllocation.repository.TripRepository;
import com.fairAllocation.cabAllocation.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllocationServiceTest {


    @Mock
    private TripRepository tripRepository;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private AllocationService allocationService;

    @Test
    void rejectAndReallocate_ShouldThrowException_WhenTripNotAllowed(){

        Trip pendingTrip = new Trip();
        pendingTrip.setId(1L);
        pendingTrip.setStatus("PENDING");

        when(tripRepository.findById(1L)).thenReturn(Optional.of(pendingTrip));

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            allocationService.rejectAndReallocate(1L);
        });

        assertEquals("Trip is not currently allocated", exception.getMessage());

    }


    @Test
    void rejectAndReallocate_ShouldPenalizeVendor(){

        Vendor v1= new Vendor(1L,"Vendor 1", 10, 10, null);
        Trip allocatedTrip= new Trip();
        allocatedTrip.setId(2L);
        allocatedTrip.setStatus("ALLOCATED");
        allocatedTrip.setAssignedVendor(v1);

        when(tripRepository.findById(2L)).thenReturn(Optional.of(allocatedTrip));

        try{
            allocationService.rejectAndReallocate(2L);
        }
        catch(Exception e){

        }

        assertNotNull(v1.getCoolOffUntil());
        assertTrue(v1.getCoolOffUntil().isAfter(LocalDateTime.now()));
        assertEquals(11, v1.getCurrentActiveCabs());
    }
}
