package com.fairAllocation.cabAllocation.service;

import com.fairAllocation.cabAllocation.entity.Trip;
import com.fairAllocation.cabAllocation.entity.Vendor;
import com.fairAllocation.cabAllocation.entity.VendorLedger;
import com.fairAllocation.cabAllocation.repository.TripRepository;
import com.fairAllocation.cabAllocation.repository.VendorLedgerRepository;
import com.fairAllocation.cabAllocation.repository.VendorRepository;
import com.fairAllocation.cabAllocation.repository.VendorZoneConfigRepository;

import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllocationService {

    private final TripRepository tripRepository;
    private final VendorRepository vendorRepository;
    private final VendorZoneConfigRepository vendorZoneConfigRepository;
    private final VendorLedgerRepository vendorLedgerRepository;

    @Transactional
    public Trip allocateTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if ("ALLOCATED".equals(trip.getStatus())) {
            throw new RuntimeException("Trip is already allocated");
        }

        // not allocated so we will fetch all the ledgers with zonetype and category

        List<VendorLedger> ledgers = vendorLedgerRepository.findLedgersForAllocationWithLock(
                trip.getZoneType(), trip.getTripCategory());

        if (ledgers.isEmpty()) {
            throw new RuntimeException("No Vendor configurations found for this zone/category");
        }
        //dd promised share to every vendor
        for (VendorLedger ledger : ledgers) {
            Double promisedPercentage = vendorZoneConfigRepository
                    .findByZoneTypeAndTripCategory(trip.getZoneType(), trip.getTripCategory())
                    .stream()
                    .filter(config -> config.getVendor().getId().equals(ledger.getVendor().getId()))
                    .findFirst()
                    .get()
                    .getPromisedPercentage();

            BigDecimal percentageToAdd = BigDecimal.valueOf(promisedPercentage);
            ;

            ledger.setOwedBalance(ledger.getOwedBalance().add(percentageToAdd));
        }


        // selecting the most owed vendor
        VendorLedger selectedLedger = ledgers.stream()
                .filter(ledger -> ledger.getVendor().getCurrentActiveCabs() > 0)
                .max(Comparator.comparing(VendorLedger::getOwedBalance)
                        .thenComparing(l -> l.getVendor().getId().doubleValue() * -1))
                .orElseThrow(() -> new RuntimeException("No vendors with availabe capacity"));


        Vendor selectedVendor = selectedLedger.getVendor();
        //update slected vendor state
        selectedLedger.setOwedBalance(selectedLedger.getOwedBalance().subtract(BigDecimal.valueOf(100)));

        selectedLedger.setActualAllocatedCount(selectedLedger.getActualAllocatedCount() + 1);

        selectedVendor.setCurrentActiveCabs(selectedVendor.getCurrentActiveCabs() - 1);
        vendorRepository.save(selectedVendor);

        trip.setAssignedVendor(selectedVendor);
        trip.setStatus("ALLOCATED");
        tripRepository.save(trip);

        return trip;

    }
}
